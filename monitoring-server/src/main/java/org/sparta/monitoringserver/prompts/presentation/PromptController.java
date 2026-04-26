package org.sparta.monitoringserver.prompts.presentation;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.monitoringserver.prompts.application.PromptService;
import org.sparta.monitoringserver.prompts.domain.query.PromptSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/prompts")
@RequiredArgsConstructor
public class PromptController {
    private final PromptService promptService;

    @GetMapping
    public Mono<String> list(PromptSearch search, @PageableDefault(size = 10) Pageable pageable, Model model) {
        return promptService.getAllPrompts(search, pageable)
                .doOnNext(page -> {
                    model.addAttribute("prompts", page.getContent());
                    model.addAttribute("page", page);
                    model.addAttribute("search", search);
                })
                .thenReturn("prompts/list");
    }

    @GetMapping("/register")
    public Mono<String> registerForm(@RequestParam(value = "name", required = false) String name, Model model) {

        return promptService.getLatestPromptForTemplate(name)
                .doOnNext(latest -> {
                    model.addAttribute("template", latest);
                    model.addAttribute("isCopy", true);
                })
                .thenReturn("prompts/register");
    }

    @PostMapping
    public Mono<String> register(@Valid Mono<PromptRequest> requestMono, Model model) {
        return requestMono
                .flatMap(request -> promptService.registerPrompt(
                        request.name(), request.version(), request.systemPrompt(), request.content()
                ))
                .thenReturn("redirect:/prompts")
                // 검증 오류 발생 시 처리
                .onErrorResume(WebExchangeBindException.class, ex -> {
                    var errorMap = ex.getBindingResult().getFieldErrors().stream()
                            .collect(Collectors.toMap(
                                    FieldError::getField,
                                    FieldError::getDefaultMessage,
                                    (existing, replacement) -> existing
                            ));

                    model.addAttribute("errors", errorMap);
                    return ex.getBindingResult().getTarget() != null
                            ? Mono.just("prompts/register").doOnNext(v -> model.addAttribute("template", ex.getBindingResult().getTarget()))
                            : Mono.just("prompts/register");
                });
    }

    @PostMapping("/{id}/activate")
    public Mono<String> activate(@PathVariable Long id) {
        return promptService.activatePrompt(id)
                .thenReturn("redirect:/prompts");
    }

    @PostMapping("/{id}/delete")
    public Mono<String> delete(@PathVariable Long id) {
        return promptService.deletePrompt(id)
                .thenReturn("redirect:/prompts");
    }
}
