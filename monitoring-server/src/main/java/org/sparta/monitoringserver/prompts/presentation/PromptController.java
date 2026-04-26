package org.sparta.monitoringserver.prompts.presentation;


import lombok.RequiredArgsConstructor;
import org.sparta.monitoringserver.prompts.application.PromptService;
import org.sparta.monitoringserver.prompts.domain.query.PromptSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/prompts")
@RequiredArgsConstructor
public class PromptController {
    private final PromptService promptService;

    public Mono<String> list(PromptSearch search, @PageableDefault(size = 10) Pageable pageable, Model model) {
        return promptService.getAllPrompts(search, pageable)
                .doOnNext(page -> {
                    model.addAttribute("page", page.getContent());
                    model.addAttribute("page", page);
                })
                .thenReturn("prompts/list");
    }
}
