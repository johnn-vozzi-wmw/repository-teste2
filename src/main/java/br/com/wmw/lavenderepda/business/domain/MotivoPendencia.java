package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class MotivoPendencia extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPMOTIVOPENDENCIA";
	private static final String EXIGE_LIBERACAO_TODOS_NIVEIS = "1";
	private static final String EXIGE_LIBERACAO_SOMENTE_NIVEL_APROVACAO = "2";
	
	public String cdEmpresa;
    public String cdRepresentante;
    public String cdMotivoPendencia;
    public String dsMotivoPendencia;
    public String flRegraLiberacao;
    public int nuPrioridadeJust;
    
    //Não persistente
    public String nuPedido;

    public MotivoPendencia() { }
    
    public MotivoPendencia(String flRegraLiberacao) {
		this.flRegraLiberacao = flRegraLiberacao;
	}

	public MotivoPendencia(String cdEmpresa, String cdRepresentante, String cdMotivoPendencia) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdMotivoPendencia = cdMotivoPendencia;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj instanceof MotivoPendencia) {
            MotivoPendencia motivoPendencia = (MotivoPendencia) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, motivoPendencia.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, motivoPendencia.cdRepresentante) &&
                ValueUtil.valueEquals(cdMotivoPendencia, motivoPendencia.cdMotivoPendencia);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdMotivoPendencia);
        return primaryKey.toString();
    }
    
    public boolean isExigeLiberacaoPassandoPorTodosNiveis() {
		return ValueUtil.valueEquals(EXIGE_LIBERACAO_TODOS_NIVEIS, flRegraLiberacao);
	}
	
	public boolean isExigeLiberacaoSomenteNivelAprovacao() {
		return ValueUtil.valueEquals(EXIGE_LIBERACAO_SOMENTE_NIVEL_APROVACAO, flRegraLiberacao);
	}

	@Override
	public String getCdDomain() {
		return cdMotivoPendencia;
	}

	@Override
	public String getDsDomain() {
		return dsMotivoPendencia;
	}

    @Override
    public String toString() {
        return getDsDomain() + " [" + getCdDomain() + "]";
    }
}