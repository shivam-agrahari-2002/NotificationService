package com.shivam.notificationservice.servicetest;

import com.shivam.notificationservice.repository.mysql.BlackListRepository;
import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.entity.mysql.BlackListEntity;
import com.shivam.notificationservice.exception.BadRequestException;
import com.shivam.notificationservice.services.BlackListService;
import com.shivam.notificationservice.services.BlacklistCachingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = "com.shivam.notificationservice")
public class BlackListServiceTest {
    @Mock
    BlackListRepository blackListRepository;
    @Mock
    BlacklistCachingService blacklistCachingService;
    @InjectMocks
    BlackListService blackListService;
    @Test
    public void testBlackListChecker_BlackListenInCache() throws Exception {
        String phoneNumber = "1234567890";
        when(blacklistCachingService.isPresent(phoneNumber)).thenReturn(true);
        boolean check = blackListService.blackListChecker(phoneNumber);
        Assertions.assertThat(check).isTrue();
    }
    @Test
    public void testBlackListChecker_BlackListenInRepository() throws Exception {
        String phoneNumber = "1234567890";
        when(blacklistCachingService.isPresent(phoneNumber)).thenReturn(false);
        when(blackListRepository.existsById(phoneNumber)).thenReturn(true).thenReturn(false);
        boolean check = blackListService.blackListChecker(phoneNumber);
        Assertions.assertThat(check).isTrue();
        check = blackListService.blackListChecker(phoneNumber);
        Assertions.assertThat(check).isFalse();
    }
    @Test
    public void testWhiteLister() throws Exception {
        String phoneNumber = "1234567890";
        when(blacklistCachingService.isPresent(phoneNumber)).thenReturn(true);
        when(blacklistCachingService.removeFromSet(phoneNumber)).thenReturn(true);
        blackListService.whiteLister(phoneNumber);

        verify(blackListRepository, times(1)).deleteById(phoneNumber);
        verify(blacklistCachingService, times(1)).removeFromSet(phoneNumber);
    }
    @Test
    public void testGetAllBlackListedNumber() throws Exception {
        List<BlackListEntity> blackListEntities = new ArrayList<>();
        blackListEntities.add(new BlackListEntity("123"));
        blackListEntities.add(new BlackListEntity("456"));
        when(blackListRepository.findAll()).thenReturn(blackListEntities);

        List<String> result = blackListService.getAllBlackListedNumber();

        Assertions.assertThat(result).containsExactly("123", "456");
    }

    @Test
    void testBlackLister_PresentInCache() throws Exception {
        String phoneNumber = "1234567890";
        when(blacklistCachingService.isPresent(phoneNumber)).thenReturn(true);

        boolean result = blackListService.blackLister(phoneNumber);

        Assertions.assertThat(result).isFalse();
        verify(blacklistCachingService).isPresent(phoneNumber);
        verify(blackListRepository, never()).existsById(phoneNumber);
        verify(blackListRepository, never()).save(any(BlackListEntity.class));
    }

    @Test
    void testBlackLister_PresentInRepository() throws Exception {
        String phoneNumber = "1234567890";
        when(blacklistCachingService.isPresent(phoneNumber)).thenReturn(false);
        when(blackListRepository.existsById(phoneNumber)).thenReturn(true);

        boolean result = blackListService.blackLister(phoneNumber);

        Assertions.assertThat(result).isTrue();
        verify(blacklistCachingService).isPresent(phoneNumber);
        verify(blackListRepository).existsById(phoneNumber);
        verify(blackListRepository).save(new BlackListEntity(phoneNumber));
    }

    @Test
    void testBlackLister_NotPresent() throws Exception {
        String phoneNumber = "1234567890";
        when(blacklistCachingService.isPresent(phoneNumber)).thenReturn(false);
        when(blackListRepository.existsById(phoneNumber)).thenReturn(false);

        boolean result = blackListService.blackLister(phoneNumber);

        Assertions.assertThat(result).isFalse();
        verify(blacklistCachingService).isPresent(phoneNumber);
        verify(blackListRepository).existsById(phoneNumber);
        verify(blackListRepository, never()).save(any(BlackListEntity.class));
    }

    @Test
    void testBlackListGiven() throws Exception {
        List<String> phoneNumbers = List.of("1230000000", "4560000000", "7890000000");
        when(blacklistCachingService.isPresent("1230000000")).thenReturn(true);
        when(blacklistCachingService.isPresent("4560000000")).thenReturn(false);
        when(blacklistCachingService.isPresent("7890000000")).thenReturn(false);
        when(blackListRepository.existsById("4560000000")).thenReturn(true);
        when(blackListRepository.existsById("7890000000")).thenReturn(false);
        String result = blackListService.blackListGiven(phoneNumbers);
        Assertions.assertThat(result).isEqualTo("Sucessfully Blacklisted");
        verify(blacklistCachingService,times(3)).isPresent(anyString());
        verify(blackListRepository,times(1)).save(any(BlackListEntity.class));
    }
    @Test
    void testBlackListGiven_invalidPhoneNumbers() throws Exception {
        List<String> phoneNumbers = List.of("1230000000", "4560000000", "789000000");
        BadRequestException exception = assertThrows(BadRequestException.class, ()->blackListService.blackListGiven(phoneNumbers));
        verify(blackListRepository, never()).existsById(anyString());
        Assertions.assertThat(exception.getResponseError().getCode()).isEqualTo(Constants.INVALID_REQUEST);
        Assertions.assertThat(exception.getResponseError().getMessage()).isEqualTo("check your phone numbers");
    }
    @Test
    void testWhiteListGiven() throws Exception {
        List<String> phoneNumbers = List.of("1230000000", "4560000000");
        when(blackListRepository.existsById("1230000000")).thenReturn(true);
        when(blackListRepository.existsById("4560000000")).thenReturn(false);

        String result = blackListService.whiteListGiven(phoneNumbers);

        verify(blackListRepository, times(1)).deleteById("1230000000");
        verify(blackListRepository, never()).deleteById("4560000000");
        Assertions.assertThat(result).isEqualTo("Sucessfully Whitelisted");
    }
    @Test
    void testWhiteListGiven_invalidPhoneNumbers() throws Exception {
        List<String> phoneNumbers = List.of("1230000000", "4560000000", "789000000");
        BadRequestException exception = assertThrows(BadRequestException.class, ()->blackListService.blackListGiven(phoneNumbers));
        verify(blackListRepository, never()).existsById(anyString());
        Assertions.assertThat(exception.getResponseError().getCode()).isEqualTo(Constants.INVALID_REQUEST);
        Assertions.assertThat(exception.getResponseError().getMessage()).isEqualTo("check your phone numbers");
    }
}
