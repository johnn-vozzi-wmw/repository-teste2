package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ClienteInativacao extends LavendereBasePersonDomain {

	public static final String TABLE_NAME = "TBLVPCLIENTEINATIVACAO";

    public ClienteInativacao() {
		super(TABLE_NAME);
	}

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
	public String flCancelado;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(cdCliente);
		return primaryKey.toString();
	}

	public ClienteInativacao(Cliente cliente) {
		super(TABLE_NAME);
		this.cdEmpresa = cliente.cdEmpresa;
		this.cdRepresentante = cliente.cdRepresentante;
		this.cdCliente = cliente.cdCliente;
	}

	public ClienteInativacao(boolean cancelado) {
		super(TABLE_NAME);
		if (cancelado) {
			this.flCancelado = ValueUtil.VALOR_SIM;
		}
	}

	@Override
	public String getCdDomain() {
		return cdCliente;
	}

	@Override
	public String getDsDomain() {
		return cdCliente;
	}
}