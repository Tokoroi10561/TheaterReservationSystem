package com.example.demo.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
