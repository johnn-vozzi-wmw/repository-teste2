package br.com.wmw.lavenderepda.business.domain;


import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.util.LavendereFileChooserBoxUtil;
import totalcross.util.Date;

public class DocumentoAnexo extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPDOCUMENTOANEXO";
	public static final String NM_COLUNA_FLTIPOALTERACAO = "flTipoAlteracao";
	public static final String NM_ENTIDADE_PEDIDO = "PEDIDO";
	public static final String NM_ENTIDADE_NOVOCLIENTE = "NOVOCLIENTE";
	public static final String NM_ENTIDADE_PROSPECT = "PROSPECT";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String nmEntidade;
    public String dsChave;
    public int cdDocumentoAnexo;
    public String nmArquivo;
    public String baArquivo;
    public Date dtDocumento;
    //--
	public String dsPath;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DocumentoAnexo) {
            DocumentoAnexo documentoAnexo = (DocumentoAnexo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, documentoAnexo.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, documentoAnexo.cdRepresentante) && 
                ValueUtil.valueEquals(nmEntidade, documentoAnexo.nmEntidade) && 
                ValueUtil.valueEquals(dsChave, documentoAnexo.dsChave) && 
                ValueUtil.valueEquals(cdDocumentoAnexo, documentoAnexo.cdDocumentoAnexo);
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
        primaryKey.append(nmEntidade);
        primaryKey.append(";");
        primaryKey.append(dsChave);
        primaryKey.append(";");
        primaryKey.append(cdDocumentoAnexo);
        return primaryKey.toString();
    }

    public static String getPathDoc() {
		return LavendereFileChooserBoxUtil.getPathDoc(new DocumentoAnexo());
	}
}