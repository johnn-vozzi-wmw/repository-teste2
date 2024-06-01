package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Date;
import totalcross.util.Vector;

public class Sac extends LavendereBaseDomain {
	
	public static final String CDSTATUSSAC_NAO_INICIADO = "1";
	public static final String CDSTATUSSAC_EM_ANDAMENTO = "2";
	public static final String CDSTATUSSAC_CONCLUIDO = "3";
	
	public static String TABLE_NAME = "TBLVPSAC";

    public String cdEmpresa;
    public String cdSac;
    public String cdTipoSac;
    public String cdSubTipoSac;
    public String dsSac;
    public String cdRepresentante;
    public String cdCliente;
    public String cdContato;
    public String cdStatusSac;
    public String nuNotaFiscal;
    public String nuPedido;
    public String cdSerie;
    public String cdUsuarioSac;
    public Date dtCadastro;
    public Date dtPrevisao;
    public Date dtConclusao;
    public String hrCadastro;
    public String hrConclusao;
    public int cdAtendimento;
    public String flStatusAlterado;
    public String flSacExibido;
    public boolean filterBySacNaoExibido;
    public Vector listProdutoSac;
    public TipoSac tipoSac;
    public ContatoCrm contatoCrm;
    public String flOrigem;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    
    //NaoPersistente
    
    public String nmContato;
    public String nuFoneContato;
    public String emailContato;
    
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Sac) {
            Sac sac = (Sac) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, sac.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, sac.cdRepresentante) && 
                ValueUtil.valueEquals(cdSac, sac.cdSac);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdSac);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdSac;
	}

	@Override
	public String getDsDomain() {
		return dsSac;
	}
	
	public String getDsTipoSac(String cdStatus) {
    	if (CDSTATUSSAC_NAO_INICIADO.equals(cdStatus)) {
    		return Messages.STATUSSAC_NAO_INICIADO;
    	} else if (CDSTATUSSAC_EM_ANDAMENTO.equals(cdStatus)) {
    		return Messages.STATUSSAC_EM_ANDAMENTO;
    	} else if (CDSTATUSSAC_CONCLUIDO.equals(cdStatus)) {
    		return Messages.STATUSSAC_CONCLUIDO;
    	}
    	return Messages.STATUSSAC_SEMSTATUS;
    }

}