-- Run BEFORE deploying the entity mappings that replace @Lob with TEXT.
-- Stop application writers and take a database backup first.
-- Assumes the application's tables are in the public schema.
-- Existing TEXT columns are untouched. NULLs and UTF-8 content are preserved.
-- Do not cast OID directly to TEXT: that would store the object ID, not its content.
-- Large objects are retained because other columns may still reference them.
BEGIN;
SET LOCAL lock_timeout = '10s';

DO $migration$
DECLARE
    target record;
    actual_type oid;
BEGIN
    FOR target IN
        SELECT * FROM (VALUES
            ('audit_logs', 'content'),
            ('brands', 'description'),
            ('products', 'description'),
            ('products', 'main_ingredients'),
            ('products', 'benefits'),
            ('return_requests', 'description'),
            ('reviews', 'content')
        ) AS columns_to_convert(table_name, column_name)
    LOOP
        SELECT a.atttypid INTO actual_type
        FROM pg_attribute a
        WHERE a.attrelid = to_regclass(format('%I.%I', 'public', target.table_name))
          AND a.attname = target.column_name
          AND a.attnum > 0 AND NOT a.attisdropped;

        IF actual_type IS NULL THEN
            RAISE EXCEPTION 'Missing column public.%.%; check the database/schema before migrating',
                target.table_name, target.column_name;
        ELSIF actual_type = 'oid'::regtype THEN
            EXECUTE format(
                'ALTER TABLE %I.%I ALTER COLUMN %I TYPE text USING convert_from(lo_get(%I), ''UTF8'')',
                'public', target.table_name, target.column_name, target.column_name);
            RAISE NOTICE 'Converted public.%.% from OID to TEXT', target.table_name, target.column_name;
        ELSIF actual_type <> 'text'::regtype THEN
            RAISE EXCEPTION 'Unexpected type % for public.%.%; transaction rolled back',
                actual_type::regtype, target.table_name, target.column_name;
        END IF;
    END LOOP;
END
$migration$;

COMMIT;
