package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.business.service.SacService;
import totalcross.util.Vector;

public class ListNovoSacWindow extends WmwWindow {

	private ScrollTabbedContainer scTabContainer;
	private ListSacForm listNovoSacForm;
	private ListSacForm listSacStatusAlteradoForm;

	public ListNovoSacWindow() throws SQLException {
		super("SACs");
		String[] abas = new String[]{"Novos SACs", "SACs Status Alterado"};
        scTabContainer = new ScrollTabbedContainer(abas);
        listNovoSacForm = new ListSacForm(new Cliente(), true, true, false, true);
        listNovoSacForm.listNovoSacWindow = this;
        listSacStatusAlteradoForm = new ListSacForm(new Cliente(), true, false, true, true);
        listSacStatusAlteradoForm.listNovoSacWindow = this;
        scTabContainer.allSameWidth = true;
		scrollable = false;
		setDefaultRect();
	}

	public void initUI() {
		super.initUI();
		UiUtil.add(this, scTabContainer , LEFT , getTop() , FILL , FILL - footerH);
		scTabContainer.setContainer(0, listNovoSacForm);
		listNovoSacForm.visibleState();
		scTabContainer.setContainer(1, listSacStatusAlteradoForm);
		listSacStatusAlteradoForm.visibleState();
	}

	public void edit(BaseDomain selectedDomain) throws SQLException {
		CadSacWindow cadSacWindow = new CadSacWindow();
		cadSacWindow.edit(selectedDomain);
		cadSacWindow.popup();
	}

	@Override
	protected void onUnpop() {
		try {
			super.onUnpop();
				Vector sacList = VectorUtil.concatVectors(listNovoSacForm.listSac, listSacStatusAlteradoForm.listSac);
				int size = sacList.size();
				for (int i = 0; i < size; i++) {
					Sac sac = (Sac) sacList.items[i];
					sac.flSacExibido = ValueUtil.VALOR_SIM;
					sac.flStatusAlterado = ValueUtil.VALOR_NAO;
					SacService.getInstance().update(sac);
				
				}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
}
