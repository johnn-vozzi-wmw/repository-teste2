package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.util.ValueUtil;

public class ProdutoDestaque extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPPRODUTODESTAQUE";

    public static final char SEPARADOR_CAMPOS = ';';
    
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdGrupoDestaque;
    public int cdEsquemaCor;
    public int cdCor;
    public String dsGrupoDestaque;
    public byte[] imIcone;
    public String flFiltroSelecionado;
    
    //Nao persistentes
    public CorSistema corSistema;

    @Override
    public boolean equals(Object obj) {
    	if (obj instanceof ProdutoDestaque) {
            ProdutoDestaque produtoDestaque = (ProdutoDestaque) obj;
			return ValueUtil.valueEquals(cdEmpresa, produtoDestaque.cdEmpresa)
					&& ValueUtil.valueEquals(cdRepresentante, produtoDestaque.cdRepresentante)
					&& ValueUtil.valueEquals(cdGrupoDestaque, produtoDestaque.cdGrupoDestaque);
        }
        return false;
    }

	public String getCdDomain() {
		return cdGrupoDestaque;
	}

	public String getDsDomain() {
		return dsGrupoDestaque;
	}

	public String getPrimaryKey() {
		StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdGrupoDestaque);
        return strBuffer.toString();
	}
	
	public boolean isFiltroSelecionadoPorPadrao() {
		return ValueUtil.valueEquals(flFiltroSelecionado, ValueUtil.VALOR_SIM);
	}
	
}