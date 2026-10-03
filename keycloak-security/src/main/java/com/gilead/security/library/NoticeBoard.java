package com.gilead.security.library;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class NoticeBoard {

    private final AtomicLong ids = new AtomicLong();
    private final CopyOnWriteArrayList<Notice> notices = new CopyOnWriteArrayList<>();

    public Notice post(String text, String author) {
        Notice notice = new Notice(ids.incrementAndGet(), text, author);
        notices.add(notice);
        return notice;
    }

    public List<Notice> all() {
        return List.copyOf(notices);
    }
}
