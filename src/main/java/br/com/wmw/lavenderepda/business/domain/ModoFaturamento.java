package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;

public class ModoFaturamento extends LavendereBaseDomain {

    public static final String TABLE_NAME = "TBLVPMODOFATURAMENTO";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdModoFaturamento;
	public String dsModoFaturamento;
	public String flExigeObservacao;
	
	public ModoFaturamento() {
		this.cdEmpresa = SessionLavenderePda.cdEmpresa;
		this.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(getClass());
	}
	
	public ModoFaturamento(String cdModoFaturamento) {
		this();
		this.cdModoFaturamento = cdModoFaturamento;
	}
	
	@Override
	public String getCdDomain() {
		return cdModoFaturamento;
	}

	@Override
	public String getDsDomain() {
		return dsModoFaturamento;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(cdModoFaturamento);
		return primaryKey.toString()
				;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj instanceof ModoFaturamento) {
            ModoFaturamento modoFaturamento = (ModoFaturamento) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, modoFaturamento.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, modoFaturamento.cdRepresentante) &&
                ValueUtil.valueEquals(cdModoFaturamento, modoFaturamento.cdModoFaturamento);
        }
        return false;
    }

	public boolean isExigeObservacao() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flExigeObservacao);
	}
}
