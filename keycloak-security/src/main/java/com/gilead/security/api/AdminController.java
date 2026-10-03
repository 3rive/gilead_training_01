package com.gilead.security.api;

import com.gilead.security.library.Notice;
import com.gilead.security.library.NoticeBoard;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final NoticeBoard noticeBoard;

    public AdminController(NoticeBoard noticeBoard) {
        this.noticeBoard = noticeBoard;
    }

    @GetMapping("/notices")
    @PreAuthorize("hasRole('admin')")
    List<Notice> notices() {
        return noticeBoard.all();
    }

    @PostMapping("/notices")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('admin')")
    Notice post(@Valid @RequestBody CreateNoticeRequest request, Authentication authentication) {
        return noticeBoard.post(request.text(), authentication.getName());
    }
}
