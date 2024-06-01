package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.UsuarioConfigService;
import totalcross.util.Vector;

public class RepresentanteComboBox extends BaseComboBox {

	public RepresentanteComboBox() {
		super(Messages.REPRESENTANTE_NOME_ENTIDADE);
	}

	public String getValue() {
		Representante representante = (Representante)getSelectedItem();
		if (representante != null) {
			return representante.cdRepresentante;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		Representante representante = new Representante();
		representante.cdRepresentante = value;
		select(representante);
	}

	public void carregaRepresentantes(String cdEmpresa) throws SQLException {
		removeAll();

		String ultimoRepLogado = ConfigInternoService.getInstance().getUltimoCdRepLogado();
		String cdRepPadraoUsuario = UsuarioConfigService.getInstance().getCdRepresentantePadrao();
		Vector repList = RepresentanteService.getInstance().getRepresentanteListByCdEmpresa(cdEmpresa);

		if (ValueUtil.isNotEmpty(repList)) {
			add(repList);
			int size = repList.size();
			setSelectedIndex(-1);
			for (int i = 0; i < size; i++) {
				Representante representante = (Representante)repList.items[i];
				if (ValueUtil.valueEquals(representante.cdRepresentante, ultimoRepLogado)) {
					setSelectedIndex(i);
					break;
				}
				if (ValueUtil.valueEquals(representante.cdRepresentante, cdRepPadraoUsuario)
						&& ValueUtil.valueEquals(ultimoRepLogado, Representante.CDREPRESENTANTE_COMBO_DEFAULT)) {
					setSelectedIndex(i);
					break;
				}
			}
			if (getValue() == null) {
				setSelectedIndex(0);
			}
		}
	}

}