package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class MotivoPendenciaJust extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPMOTIVOPENDENCIAJUST";

	private static final String FLUSAOBSERVACAO_OBRIGATORIO = "1";
    private static final String FLUSAOBSERVACAO_OPCIONAL = "2";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdMotivoPendencia;
    public String cdMotivoPendenciaJust;
    public String dsMotivoPendenciaJust;
    public String flUsaObservacao;
    
    public MotivoPendenciaJust() { }
    
    public MotivoPendenciaJust(String cdEmpresa, String cdRepresentante, String cdMotivoPendencia) {
    	this.cdEmpresa = cdEmpresa;
    	this.cdRepresentante = cdRepresentante;
    	this.cdMotivoPendencia = cdMotivoPendencia;
    }
    
    public MotivoPendenciaJust(String cdEmpresa, String cdRepresentante, String cdMotivoPendencia, String cdMotivoPendenciaJust) {
    	this(cdEmpresa, cdRepresentante, cdMotivoPendencia);
    	this.cdMotivoPendenciaJust = cdMotivoPendenciaJust;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MotivoPendenciaJust) {
            MotivoPendenciaJust motivoPendenciaJust = (MotivoPendenciaJust) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, motivoPendenciaJust.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, motivoPendenciaJust.cdRepresentante) && 
                ValueUtil.valueEquals(cdMotivoPendencia, motivoPendenciaJust.cdMotivoPendencia) && 
                ValueUtil.valueEquals(cdMotivoPendenciaJust, motivoPendenciaJust.cdMotivoPendenciaJust);
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
        primaryKey.append(";");
        primaryKey.append(cdMotivoPendenciaJust);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() { return cdMotivoPendenciaJust;	}

	@Override
	public String getDsDomain() { return dsMotivoPendenciaJust;	}

	public boolean isHabilitaObservacao() {
        return isObservacaoOpcional() || isObservacaoObrigatoria();
	}
	
	public boolean isObservacaoNaoUtilizada() {
		return flUsaObservacao == null || ValueUtil.valueEquals(flUsaObservacao, ValueUtil.VALOR_NAO);
	}

	public boolean isObservacaoOpcional() {
		return ValueUtil.valueEquals(flUsaObservacao, FLUSAOBSERVACAO_OPCIONAL);
	}

	public boolean isObservacaoObrigatoria() {
		return ValueUtil.valueEquals(flUsaObservacao, FLUSAOBSERVACAO_OBRIGATORIO);
	}

    @Override
    public String toString() {
        return getDsDomain() + " [" + getCdDomain() + "]";
    }
}