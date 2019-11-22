package xyz.anythings.sorter.service.tcp;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.anythings.sorter.tcp.model.ITcpModel;
import xyz.anythings.sorter.tcp.service.ITcpService;
import xyz.elidom.orm.IQueryManager;

@Component
public class SorterControlService implements ITcpService{
	@Autowired
	protected IQueryManager queryManager;
	
	@Override
	public void doService(IoSession session, ITcpModel request) {
		
	}

}
