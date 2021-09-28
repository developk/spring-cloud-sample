package com.gndbiz.modules.core.domain.loginHis;

import com.gndbiz.modules.core.support.service.BaseCrudService;
import org.springframework.stereotype.Service;

@Service
public class LoginHistoryService extends BaseCrudService<LoginHistory> {

	private final LoginHistoryRepository loginHistoryRepository;

	public LoginHistoryService(LoginHistoryRepository loginHistoryRepository) {
		super(loginHistoryRepository);
		this.loginHistoryRepository = loginHistoryRepository;
	}

}
