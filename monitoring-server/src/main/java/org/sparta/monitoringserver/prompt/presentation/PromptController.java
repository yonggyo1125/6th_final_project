package org.sparta.monitoringserver.prompt.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.monitoringserver.prompt.application.PromptService;
import org.sparta.monitoringserver.prompt.domain.query.PromptSearch;
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
                .flatMap(page -> {
                    model.addAttribute("prompts", page.getContent());
                    model.addAttribute("page", page);
                    model.addAttribute("search", search);
                    return Mono.just("prompts/list");
                });
    }


    @GetMapping("/register")
    public Mono<String> registerForm(@RequestParam(value = "name", required = false) String name, Model model) {
        return promptService.getLatestPromptForTemplate(name)
                .flatMap(latest -> {
                    model.addAttribute("template", latest);
                    model.addAttribute("isCopy", true);
                    return Mono.just("prompts/register");
                })
                // 데이터가 없는 순수 신규 등록일 경우를 위한 처리
                .switchIfEmpty(Mono.just("prompts/register"));
    }

    /**
     * 프롬프트 등록 실행 (확장된 파라미터 반영)
     */
    @PostMapping
    public Mono<String> register(@Valid Mono<PromptRequest> requestMono, Model model) {
        return requestMono
                .flatMap(request -> promptService.registerPrompt(
                        request.name(),
                        request.version(),
                        request.modelName(),
                        request.description(),
                        request.maxTokens(),
                        request.temperature(),
                        request.systemPrompt(),
                        request.content()
                ))
                .thenReturn("redirect:/prompts")
                .onErrorResume(WebExchangeBindException.class, ex -> {
                    var errorMap = ex.getBindingResult().getFieldErrors().stream()
                            .collect(Collectors.toMap(
                                    FieldError::getField,
                                    FieldError::getDefaultMessage,
                                    (existing, replacement) -> existing
                            ));

                    model.addAttribute("errors", errorMap);

                    // 검증 실패 시 입력했던 데이터를 다시 폼에 채워줌
                    Object target = ex.getBindingResult().getTarget();
                    if (target != null) {
                        model.addAttribute("template", target);
                    }

                    return Mono.just("prompts/register");
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