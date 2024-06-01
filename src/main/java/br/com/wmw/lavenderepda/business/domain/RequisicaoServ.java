package br.com.wmw.lavenderepda.business.domain;
import br.com.wmw.framework.business.domain.BaseDomain;
import totalcross.util.Date;
import totalcross.util.Vector;

public class RequisicaoServ extends BaseDomain  {
	
    public static String TABLE_NAME = "TBLVPREQUISICAOSERV";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdRequisicaoServ;
    public Date   dtRequisicaoServ;
    public String hrRequisicaoServ;
    public String dsObservacao;
    public String cdCliente;
    public String nuPedido;
    public String flOrigemPedido;
    public RequisicaoServTipo tipoServ;
    public RequisicaoServMotivo motivoServ;
    public Cliente cliente;
    public String cdUsuarioCriacao;

    
    //Não persistente
    public Date dtInicioFilter;
    public Date dtFimFilter;
    public boolean obrigaCliente;
    public boolean obrigaPedido;
    private Vector requisicaoServImagemList;
    private Vector requisicaoServImagemExcluidaList;
    
    public RequisicaoServ() {
    	cliente = new Cliente();
    	motivoServ = new RequisicaoServMotivo();
    	tipoServ = new RequisicaoServTipo();
    }
 
    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdRequisicaoServ);
        return primaryKey.toString();
    }

	public Vector getRequisicaoServImagemList() {
		if (requisicaoServImagemList == null) {
			requisicaoServImagemList = new Vector();
		}
		return requisicaoServImagemList;
	}

	public void setRequisicaoServImagemList(Vector requisicaoServImagemList) {
		this.requisicaoServImagemList = requisicaoServImagemList;
	}

	public Vector getRequisicaoServImagemExcluidaList() {
		if (requisicaoServImagemExcluidaList == null) {
			requisicaoServImagemExcluidaList = new Vector();
		}
		return requisicaoServImagemExcluidaList;
	}

	public void setRequisicaoServImagemExcluidaList(Vector requisicaoServImagemExcluidaList) {
		this.requisicaoServImagemExcluidaList = requisicaoServImagemExcluidaList;
	}

}