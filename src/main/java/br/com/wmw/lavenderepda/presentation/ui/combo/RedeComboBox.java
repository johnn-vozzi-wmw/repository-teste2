package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Rede;
import br.com.wmw.lavenderepda.business.service.RedeService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RedeDbxDao;
import totalcross.util.Vector;

public class RedeComboBox extends BaseComboBox {
	
	public RedeComboBox() throws SQLException {
		super(Messages.REDE_NOME_ENTIDADE);
		this.defaultItemType = DefaultItemType_ALL;
		carregaRedes();
	}
	
	public void setValue(String value) {
		if (value != null) {
			Rede rede = new Rede();
			rede.cdEmpresa = SessionLavenderePda.cdEmpresa;
			rede.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Rede.class);
			rede.cdRede = value;
			select(rede);
		} else {
			setSelectedIndex(-1);
		}
	}
	
	public String getValue() {
		Rede rede = (Rede) getSelectedItem();
		if (rede != null) {
			return rede.cdRede;
		}
		return ValueUtil.VALOR_NI;
	}
	
	private void carregaRedes() throws SQLException {
		Rede redeFilter = new Rede();
		redeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		redeFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Rede.class);
		Vector redeList = RedeDbxDao.getInstance().findAllRedeComboByExample(redeFilter);
		redeList.qsort();
		add(redeList);
		if (redeList != null && redeList.size() > 1) {
			setSelectedIndex(0);
		}
	}

}
