package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class MenuCategoria extends BaseDomain {

	public static String TABLE_NAME = "TBLVPMENUCATEGORIA";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdMenu;
	public String cdMenuPai;
	public String dsMenu;
	public byte[] imFoto;
	public String nmFoto;
	public String flMostraProdutos;
	public String flMostraProdutosFilhos;
	public String dtAlteracao;
	public String hrAlteracao;
	public String flPrincipal;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdMenu + ";" + cdUsuario;
	}

	public boolean mostraProdutos() {
		return ValueUtil.getBooleanValue(flMostraProdutos);
	}

	public boolean mostraProdutosFilhos() {
		return ValueUtil.getBooleanValue(flMostraProdutosFilhos);
	}

	public boolean isPrincipal() {
		return ValueUtil.getBooleanValue(flPrincipal);
	}

}
