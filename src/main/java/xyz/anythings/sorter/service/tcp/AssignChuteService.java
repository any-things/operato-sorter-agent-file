package xyz.anythings.sorter.service.tcp;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import xyz.anythings.sorter.tcp.model.ITcpModel;
import xyz.anythings.sorter.tcp.service.ITcpService;

@Component
public class AssignChuteService implements ITcpService{
	@Override
	public void doService(IoSession session, ITcpModel request) {
		
	}

}
