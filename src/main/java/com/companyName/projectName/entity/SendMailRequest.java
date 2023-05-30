package com.companyName.projectName.entity;

import java.util.List;
import javax.validation.constraints.NotEmpty;

public class SendMailRequest {
    //@NotNull：不能为null，但可以为empty
    //@NotEmpty：不能为null，而且长度必须大于0
    //@NotBlank：只能作用在String上，不能为null，而且调用trim()后，长度必须大于0
    @NotEmpty
    private String subject;

    @NotEmpty
    private String content;

    @NotEmpty
    private List<String> receivers;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }
}
