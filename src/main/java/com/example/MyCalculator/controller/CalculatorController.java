package com.example.MyCalculator.controller;

import com.example.MyCalculator.dto.UserSignupDto;
import com.example.MyCalculator.model.Log;
import com.example.MyCalculator.model.User;
import com.example.MyCalculator.service.CalculatorService;
import com.example.MyCalculator.service.LogService;
import com.example.MyCalculator.service.UserLoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/calculator")
@RequiredArgsConstructor
public class CalculatorController {
    private final LogService logService;
    private final CalculatorService calculatorService;
    private final UserLoginService userLoginService;

    private Boolean loginOrNot(HttpSession httpsession) {
        return httpsession.getAttribute("user") != null;
    }

    @GetMapping
    public String showCalculator(HttpSession httpSession, Model model) {

        if (!model.containsAttribute("inputPanels")) {
            model.addAttribute("inputPanels", "");
        }
        if (!model.containsAttribute("result")) {
            model.addAttribute("result", "");
        }
        if (!model.containsAttribute("underPoint")) {
            model.addAttribute("underPoint", -1);
        }

        // 로그인 이후부터 동작
        if(loginOrNot(httpSession)) {
            User user = (User) httpSession.getAttribute("user");
            Log log = new Log();
            log.setUserId(user.getId());

            model.addAttribute("user", user);
            model.addAttribute("loginUserName", user.getUserName());

            List<Log> listNotFixed = logService.findLogsNotFixedByUserId(log);
            model.addAttribute("notFixedList", listNotFixed);

            List<Log> listFixed = logService.findLogsFixedByUserId(log);
            model.addAttribute("fixedList", listFixed);
        }

        model.addAttribute("userSignupDto", new UserSignupDto());

        return "calculator";
    }

    @PostMapping("/makeInputPanels")
    public String makeInputPanels(@RequestParam("inputPanels") String inputPanels,
                                  @RequestParam("inputButton") String inputButton,
                                  @RequestParam("underPoint") Integer underPoint,
                                  RedirectAttributes redirectAttributes) {

        String updateInputPanels = inputPanels + inputButton;
        redirectAttributes.addFlashAttribute("inputPanels", updateInputPanels);
        redirectAttributes.addFlashAttribute("result", "");
        redirectAttributes.addFlashAttribute("underPoint", underPoint);

        return "redirect:/calculator";
    }

    @PostMapping("/deleteInputPanels")
    public String deleteInputPanels(@RequestParam("inputPanels") String inputPanels,
                                    @RequestParam("underPoint") Integer underPoint,
                                    RedirectAttributes redirectAttributes) {

        String beforeInputPanels = calculatorService.deleteInputPanels(inputPanels);
        redirectAttributes.addFlashAttribute("inputPanels", beforeInputPanels);
        redirectAttributes.addFlashAttribute("result", "");
        redirectAttributes.addFlashAttribute("underPoint", underPoint);

        return "redirect:/calculator";
    }

    @PostMapping("/allDeleteInputPanels")
    public String allDeleteInputPanels(@RequestParam("underPoint") Integer underPoint,
                                       RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("inputPanels", "");
        redirectAttributes.addFlashAttribute("result", "");
        redirectAttributes.addFlashAttribute("underPoint", underPoint);

        return "redirect:/calculator";
    }

    @PostMapping("/makeResult")
    public String makeRes(@RequestParam("inputPanels") String inputPanels,
                          @RequestParam("underPoint") Integer underPoint,
                          HttpSession httpSession,
                          RedirectAttributes redirectAttributes) {

        try {
            redirectAttributes.addFlashAttribute("inputPanels", inputPanels);
            redirectAttributes.addFlashAttribute("result", calculatorService.updateResult(inputPanels,underPoint));
            redirectAttributes.addFlashAttribute("underPoint", underPoint);

            // 로그인 이후부터 동작
            if(loginOrNot(httpSession)) {
                logService.create(inputPanels, httpSession, underPoint);
            }
            return "redirect:/calculator";

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("inputPanels", inputPanels);
            redirectAttributes.addFlashAttribute("result", "Error");
            redirectAttributes.addFlashAttribute("underPoint", underPoint);
            redirectAttributes.addFlashAttribute("calError", e.getMessage());

            return "redirect:/calculator";
        }
    }

    @PostMapping("/underPointUpDown")
    public String changeShowPoint(@RequestParam("inputPanels") String inputPanels,
                                  @RequestParam("result") String result,
                                  @RequestParam("underPoint") Integer underPoint,
                                  @RequestParam(required = false) Integer underPointUpButton,
                                  @RequestParam(required = false) Integer underPointDownButton,
                                  @RequestParam(required = false) Integer underPointDefaultButton,
                                  RedirectAttributes redirectAttributes) {
        Integer newUnderPoint = 0;

        if(underPointDefaultButton != null) {
            newUnderPoint = -1;
        }
        if(underPointUpButton != null) {
            newUnderPoint = underPoint + 1;
        }
        if (underPointDownButton != null) {
            newUnderPoint = underPoint - 1;
        }

        if(result == ""){
            redirectAttributes.addFlashAttribute("result", "");
        } else {
            redirectAttributes.addFlashAttribute("result", calculatorService.updateResult(inputPanels,newUnderPoint));
        }

        redirectAttributes.addFlashAttribute("inputPanels", inputPanels);
        redirectAttributes.addFlashAttribute("underPoint", newUnderPoint);

        return "redirect:/calculator";
    }

    @PostMapping("/fixedRes")
    public String checkFixed(@RequestParam("inputPanels") String inputPanels,
                             @RequestParam("result") String result,
                             @RequestParam("underPoint") Integer underPoint,
                             @RequestParam("logId") Integer logId,
                             RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("inputPanels", inputPanels);
        redirectAttributes.addFlashAttribute("result", result);
        redirectAttributes.addFlashAttribute("underPoint", underPoint);

        logService.updateFixed(logId);

        return "redirect:/calculator";
    }

    @PostMapping("/fixedDelete")
    public String deleteFixed(@RequestParam("inputPanels") String inputPanels,
                              @RequestParam("result") String result,
                              @RequestParam("underPoint") Integer underPoint,
                              @RequestParam("logId") Integer logId,
                              RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("inputPanels", inputPanels);
        redirectAttributes.addFlashAttribute("result", result);
        redirectAttributes.addFlashAttribute("underPoint", underPoint);

        logService.delete(logId);

        return "redirect:/calculator";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute UserSignupDto userSignupDto,
                          BindingResult bindingResult,
                          HttpSession httpSession,
                          Model model) {
        try {
            User user = userLoginService.loginCheck(userSignupDto);

            // 로그인시 세션에 추가되는 것들
            httpSession.setAttribute("user", user);

            return "redirect:/calculator";

        } catch (IllegalArgumentException e) {
            model.addAttribute("loginError", "아이디나 패스워드가 올바르지 않습니다.");

            return "calculator";
        } catch (EmptyResultDataAccessException e) {
            model.addAttribute("loginError", "존재하지 않는 아이디 입니다.");

            return "calculator";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession httpsession, Model model) {
        httpsession.invalidate();

        return "redirect:/calculator";
    }
}
