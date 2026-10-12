package com.thinh.cosmetic.config;

import com.thinh.cosmetic.repository.account.AccountRepository;
import com.thinh.cosmetic.repository.account.RoleRepository;
import com.thinh.cosmetic.repository.store.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {
    @Spy
    private AppProperties properties = new AppProperties();
    @Mock
    private RoleRepository roles;
    @Mock
    private AccountRepository accounts;
    @Mock
    private StoreRepository stores;
    @InjectMocks
    private DataSeeder seeder;

    @Test
    void disabledSeederDoesNotAccessTheDatabase() {
        properties.getSeed().setEnabled(false);
        seeder.run();
        verifyNoInteractions(roles, accounts, stores);
    }

    @Test
    void existingDataIsStillSkipped() {
        when(roles.count()).thenReturn(1L);
        when(accounts.count()).thenReturn(1L);
        when(stores.count()).thenReturn(1L);
        seeder.run();
        verify(roles).count();
        verify(accounts).count();
        verify(stores).count();
        verifyNoMoreInteractions(roles, accounts, stores);
    }
}
