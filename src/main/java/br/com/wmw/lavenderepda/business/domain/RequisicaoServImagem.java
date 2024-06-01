package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;

public class RequisicaoServImagem extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPREQUISICAOSERVIMAGEM";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdRequisicaoServ;
    public String nmImagem;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RequisicaoServImagem) {
            RequisicaoServImagem requisicaoServImagem = (RequisicaoServImagem) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, requisicaoServImagem.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, requisicaoServImagem.cdRepresentante) && 
                ValueUtil.valueEquals(cdRequisicaoServ, requisicaoServImagem.cdRequisicaoServ) && 
                ValueUtil.valueEquals(nmImagem, requisicaoServImagem.nmImagem);
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
        primaryKey.append(cdRequisicaoServ);
        primaryKey.append(";");
        primaryKey.append(nmImagem);
        return primaryKey.toString();
    }

    public static String getPathImg() {
		return FotoUtil.getPathImg(RequisicaoServImagem.class);
	}
}