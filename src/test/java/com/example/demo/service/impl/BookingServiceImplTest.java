package com.example.demo.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.constant.BookingStatus;
import com.example.demo.dto.BookingDetailsDto;
import com.example.demo.dto.BookingDto;
import com.example.demo.entity.Stage;
import com.example.demo.entity.TicketType;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.StageRepository;
import com.example.demo.repository.TicketTypeRepository;
import com.example.demo.service.StaffService;
import com.example.demo.service.TicketTypeService;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

	@Mock
	private BookingRepository bookingRepository;
	@Mock
	private StageRepository stageRepository;
	@Mock
	private TicketTypeRepository ticketTypeRepository;
	@Mock
	private TicketTypeService ticketTypeService;
	@Mock
	private StaffService staffService;
	
//	テスト対象のインスタンス
	@InjectMocks
	private BookingServiceImpl bookingService;
	
	@Test
	void nomal() {
		BookingDto dto = new BookingDto();
		dto.setStageId(1L);
		dto.setStaffId(null);
		
		BookingDetailsDto detailDto = new BookingDetailsDto();
		detailDto.setTicketTypeId(10L);
		detailDto.setQuantity(2);
		dto.setBookingDetails(List.of(detailDto));
		
		Stage mockStage = new Stage();
		mockStage.setCapacity(10);
		
		TicketType mockTicketType = new TicketType();
		
		
		when(stageRepository.findById(1L)).thenReturn(Optional.of(mockStage));
		when(bookingRepository.findByStageId(eq(1L), eq(BookingStatus.RESERVED))).thenReturn(List.of());
		when(ticketTypeRepository.findById(10L)).thenReturn(Optional.of(mockTicketType));
		
		String resultUuid = bookingService.createBooking(dto);
		
		assertThat(resultUuid).isNotNull();
		
		verify(bookingRepository, times(1)).save(any());
	}
}
