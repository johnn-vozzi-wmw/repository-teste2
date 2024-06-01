package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ProdutoConcorrente extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPPRODUTOCONCORRENTE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProdutoConcorrente;
	public String cdConcorrente;
    public String dsProdutoConcorrente;

    
    public ProdutoConcorrente() {
		super();
	}

	public ProdutoConcorrente(String cdEmpresa, String cdRepresentante, String cdProdutoConcorrente, String cdConcorrente) {
		super();
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdProdutoConcorrente = cdProdutoConcorrente;
		this.cdConcorrente = cdConcorrente;
	}

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoConcorrente) {
            ProdutoConcorrente produtoConcorrente = (ProdutoConcorrente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtoConcorrente.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, produtoConcorrente.cdRepresentante) && 
                ValueUtil.valueEquals(cdProdutoConcorrente, produtoConcorrente.cdProdutoConcorrente) && 
                ValueUtil.valueEquals(cdConcorrente, produtoConcorrente.cdConcorrente);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdProdutoConcorrente);
        primaryKey.append(";");
        primaryKey.append(cdConcorrente);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdProdutoConcorrente;
	}

	@Override
	public String getDsDomain() {
		return dsProdutoConcorrente;
	}

    
}