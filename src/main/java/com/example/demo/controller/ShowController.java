package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.constant.ShowOption;
import com.example.demo.dto.ShowDto;
import com.example.demo.dto.StaffDto;
import com.example.demo.dto.StageDto;
import com.example.demo.dto.TicketTypeDto;
import com.example.demo.entity.Show;
import com.example.demo.entity.Staff;
import com.example.demo.entity.Stage;
import com.example.demo.entity.TicketType;
import com.example.demo.service.ShowService;
import com.example.demo.service.StaffService;
import com.example.demo.service.StageService;
import com.example.demo.service.TicketTypeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/show")
@RequiredArgsConstructor
@Slf4j
public class ShowController {
	
	private final ShowService showService;
	private final StaffService staffService;
	private final StageService stageService;
	private final TicketTypeService ticketTypeService;

	@GetMapping("/form/{troupeId}")
	public String showForm(Model model,
						   @PathVariable Long troupeId) {
		log.info("show/formの提示");
		ShowDto showDto = new ShowDto();
		
		formSetup(showDto);
		
		model.addAttribute("troupeId", troupeId);
		
		model.addAttribute("showDto", showDto);		
		model.addAttribute("showOptions", ShowOption.values());
		
		return "show/form";
	}
	
	@PostMapping("/confirm/{troupeId}")
	public String confirm(@Valid@ModelAttribute ShowDto showDto,
						  BindingResult bindingResult,
						  HttpSession httpSession,
						  @PathVariable Long troupeId,
						  Model model) {
		log.info("フォーム確認メソッド開始");
		model.addAttribute("troupeId", troupeId);

		model.addAttribute("showOptions", ShowOption.values());
		if(bindingResult.hasErrors()) {
			log.warn("バリデーションエラー");
			bindingResult.getAllErrors().forEach(error -> {
				System.out.println(error.getDefaultMessage());
			});
			
			formSetup(showDto);
			
			model.addAttribute("showDto", showDto);
			
			return "show/form";
		}
		httpSession.setAttribute("showDto", showDto);
		log.info("セッション保存完了");
		return "show/confirm";
	}
	
	@PostMapping("/register/{troupeId}")
	public String register(HttpSession httpSession,
							 @PathVariable Long troupeId,
							 RedirectAttributes redirectAttributes) {
		log.info("フォーム登録メソッド開始");
		ShowDto showDto = (ShowDto)httpSession.getAttribute("showDto");
		
		showService.createShow(showDto, troupeId);
		log.info("データの保存");
		
		httpSession.removeAttribute("showDto");
		log.info("セッションの削除");
		
		return "redirect:/show/complete/" + troupeId;
	}
	
	@GetMapping("/complete/{troupeId}")
	public String complete(@PathVariable Long troupeId, Model model) {
		log.info("フォーム完了");
		return "show/complete";
	}
	
	@GetMapping("/update/form/{showId}")
	public String showUpdateForm(@PathVariable Long showId, Model model) {
		log.info("編集フォーム提示メソッド開始");
		Show show = showService.getById(showId);
		ShowDto showDto = ShowDto.fromEntity(show);
		
		formSetup(showId, showDto);
		
		model.addAttribute("showDto", showDto);
		model.addAttribute("showId", showId);

		model.addAttribute("showOptions", ShowOption.values());
		
		log.info("編集フォーム提示ページを投げる");
		return "show/update/form";
	}
	
	@PostMapping("/update/confirm/{showId}")
	public String confirmUpdate(@Valid@ModelAttribute ShowDto showDto,
			  				  BindingResult bindingResult,
			  				  HttpSession httpSession,
			  				  @PathVariable Long showId,
			  				  Model model) {
		log.info("編集フォーム確認メソッド開始");
		model.addAttribute("showId", showId);

		model.addAttribute("showOptions", ShowOption.values());
		if(bindingResult.hasErrors()) {
			log.warn("バリデーションエラー");
			bindingResult.getAllErrors().forEach(error -> {
				System.out.println(error.getDefaultMessage());
			});
			formSetup(showId, showDto);
			
			model.addAttribute("showDto", showDto);
			return "show/update/form";
		}
		
		httpSession.setAttribute("showDto", showDto);
		log.info("セッション保存");
		return "show/update/confirm";
	}
	
	@PostMapping("/update/register/{showId}")
	public String registerUpdate(HttpSession httpSession,
							   RedirectAttributes redirectAttributes) {
		log.info("編集フォーム更新メソッド開始");
		ShowDto showDto = (ShowDto)httpSession.getAttribute("showDto");
		
		Long troupeId = showService.updateShow(showDto);
		log.info("編集データ保存");
		httpSession.removeAttribute("showDto");
		log.info("セッション削除");
		
		return "redirect:/show/complete/" + troupeId;
	}
	
	@PostMapping("/delete/{showId}")
	public String delete(@PathVariable Long showId, Model model, RedirectAttributes redirectAttributes) {
		log.info("削除メソッド開始");
		model.addAttribute("showId", showId);
		
		Show show = showService.getById(showId);
		
		Long troupeId = show.getTroupe().getId();
		
		showService.deleteShow(showId);
		log.info("{}のデータ削除。公演ID: {}", show.getTitle(), show.getId());
		
		redirectAttributes.addFlashAttribute("successMessage", "予約を完了しました。");
		
		return "redirect:/show/list/" + troupeId;
	}
	
	private void formSetup(ShowDto showDto) {
		showDto.getTicketTypes().add(new TicketTypeDto());
		showDto.getStaffs().add(new StaffDto());
		showDto.getStages().add(new StageDto());
	}
	
	private void formSetup(Long showId, ShowDto showDto) {
		
		List<TicketType> ticketTypes = ticketTypeService.getAllTicketTypeByShowId(showId);
		List<TicketTypeDto> ticketTypeDtos = new ArrayList<>();
		for(TicketType ticketType : ticketTypes) {
			TicketTypeDto ticketTypeDto = TicketTypeDto.fromEntity(ticketType);
			ticketTypeDtos.add(ticketTypeDto);
		}
		showDto.setTicketTypes(ticketTypeDtos);
		
		List<Stage> stages = stageService.getAllStageByShowId(showId);
		List<StageDto> stageDtos = new ArrayList<>();
		for(Stage stage : stages) {
			StageDto stageDto = StageDto.fromEntity(stage);
			stageDtos.add(stageDto);
		}
		showDto.setStages(stageDtos);
		
		List<Staff> staffs = staffService.getAllStaffByShowId(showId);
		List<StaffDto> staffDtos = new ArrayList<>();
		if(staffs.isEmpty()) {
			staffDtos.add(new StaffDto());
			
			showDto.setStaffs(staffDtos);	
		}else {
			for(Staff staff : staffs) {
				StaffDto staffDto = StaffDto.fromEntity(staff);
				staffDtos.add(staffDto);
			}
			showDto.setStaffs(staffDtos);
		}
	}

	@GetMapping("/list/{troupeId}")
	public String showList(Model model, @PathVariable Long troupeId) {
		
		List<Show> showList = showService.findAllActive(troupeId);
		
		model.addAttribute("troupeId", troupeId);		
		model.addAttribute("showList", showList);
		
		
		return "show/list";
	}
	
	@GetMapping("/detail/{showId}")
	public String showDetail(@PathVariable Long showId, Model model) {
		
		Show show = showService.getById(showId);
		model.addAttribute("show", show);
		model.addAttribute("showId", showId);
		
		return "show/detail";
	}
}
