package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescProgConfigFaDes extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPDESCPROGCONFIGFADES";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdDescProgressivo;
    public int qtFamilia;
    public double vlPctDescProg;
    public String dsFaixa;
    public String flTipoAlteracao;

    // Não Persistentes
    public DescProgFamilia descProgFamilia;
    public DescProgFaixa descProgFaixa;

	public DescProgConfigFaDes() { super(TABLE_NAME); }

    public DescProgConfigFaDes(final String cdEmpresa, final String cdRepresentante, final String cdDescProgressivo) {
	    super(TABLE_NAME);
	    this.cdEmpresa = cdEmpresa;
	    this.cdRepresentante = cdRepresentante;
	    this.cdDescProgressivo = cdDescProgressivo;
	}

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DescProgConfigFaDes)) return false;
        DescProgConfigFaDes descProgressivoConfig = (DescProgConfigFaDes) obj;
        return ValueUtil.valueEquals(cdEmpresa, descProgressivoConfig.cdEmpresa)
            && ValueUtil.valueEquals(cdRepresentante, descProgressivoConfig.cdRepresentante)
            && ValueUtil.valueEquals(cdDescProgressivo, descProgressivoConfig.cdDescProgressivo)
        && ValueUtil.valueEquals(qtFamilia, descProgressivoConfig.qtFamilia);
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdDescProgressivo);
        primaryKey.append(";");
        primaryKey.append(qtFamilia);
        return primaryKey.toString();
    }

}