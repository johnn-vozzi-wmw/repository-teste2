package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.MensagemExcec;
import br.com.wmw.framework.integration.dao.MensagemExcecDbxDao;
import totalcross.sql.ResultSet;

public class MensagemExcecLavendereDao extends MensagemExcecDbxDao  {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MensagemExcec();
	}

	public MensagemExcecLavendereDao() {
		super("TBLVPMENSAGEMEXCEC");
	}
	
	public static MensagemExcecDbxDao getInstance() {
        if (instance == null) {
            instance = new MensagemExcecLavendereDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	MensagemExcec mensagemExcec = new MensagemExcec();
        mensagemExcec.rowKey = rs.getString("rowkey");
        mensagemExcec.cdSistema = rs.getInt("cdSistema");
        mensagemExcec.cdMensagemExcec = rs.getString("cdMensagemExcec");
        mensagemExcec.dsMensagemExcec = rs.getString("dsMensagemExcec");
    	return mensagemExcec;
    }

}
