#!/usr/bin/env python3
"""Render local PlantUML files; no upload, application startup or DB access."""
import subprocess
import sys
from pathlib import Path

root=Path(__file__).resolve().parents[4]
jar=Path(sys.argv[1]).expanduser().resolve() if len(sys.argv)>1 else None
if jar is None or not jar.is_file():
    raise SystemExit('Usage: python3 docs/uml/use-case/traceability/render.py /path/to/plantuml.jar')
files=[str(p) for p in sorted((root/'docs/uml/use-case').rglob('*.puml'))]
base=['java','-Djava.awt.headless=true','-DPLANTUML_LIMIT_SIZE=10000','-jar',str(jar)]
subprocess.run(base+['--check-syntax']+files,check=True,cwd=root)
for output in ['svg','png']:
    subprocess.run(base+['--format',output,'--threads','4']+files,check=True,cwd=root)
subprocess.run([sys.executable,str(Path(__file__).with_name('build_reports.py'))],check=True,cwd=root)
