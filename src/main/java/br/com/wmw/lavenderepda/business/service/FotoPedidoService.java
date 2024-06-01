package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FotoPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoPedidoDbxDao;
import totalcross.util.Vector;

public class FotoPedidoService extends CrudService {

    private static FotoPedidoService instance = null;
    
    private FotoPedidoService() {
        //--
    }
    
    public static FotoPedidoService getInstance() {
        if (instance == null) {
            instance = new FotoPedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return FotoPedidoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }
    
    public FotoPedido insereFotoPedido(Pedido pedido, String nmFoto, int cdFotoPedido) throws SQLException {
    	FotoPedido fotoPedido = getDefaultFotoPedidoFilter(pedido);
		fotoPedido.nmFoto = nmFoto;
		fotoPedido.nuTamanho = FotoUtil.getTamanhoFoto(nmFoto, FotoPedido.getPathImg());
		fotoPedido.dtModificacao = DateUtil.getCurrentDate();
		insert(fotoPedido);
		return fotoPedido;
	}

    public int getSequencialCdFotoPedido(Pedido pedido) throws SQLException {
    	FotoPedido fotoPedido = getDefaultFotoPedidoFilter(pedido);
		return countByExample(fotoPedido);
	}
    
    public void excluiFotoPedido(Pedido pedido, String nmFoto, boolean removeFromList) throws SQLException {
    	FotoPedido fotoPedido = getDefaultFotoPedidoFilter(pedido);
		fotoPedido.nmFoto = nmFoto;
    	delete(fotoPedido);
    	FileUtil.deleteFile(FotoPedido.getPathImg() + "/" + nmFoto);
    	if (removeFromList) {
    		pedido.fotoList.removeElement(fotoPedido);
    	}
	}
    
    public boolean isPermiteExcluirFoto(Pedido pedido, String nmFoto) throws SQLException {
    	FotoPedido fotoPedidoFilter = getDefaultFotoPedidoFilter(pedido);
		fotoPedidoFilter.nmFoto = nmFoto;
    	FotoPedido fotoPedido = (FotoPedido) findByRowKey(fotoPedidoFilter.getRowKey());
    	return BaseDomain.FLTIPOALTERACAO_ALTERADO.equals(fotoPedido.flTipoAlteracao);
	}
    
    public String findRSFotoPedidoNaoEnviado(Pedido pedido) throws SQLException {
		FotoPedido fotoPedidoFilter = getDefaultFotoPedidoFilter(pedido);
		fotoPedidoFilter.flEnviadoServidor = ValueUtil.VALOR_NAO;
		return FotoPedidoDbxDao.getInstance().findAllByExampleSql(fotoPedidoFilter);
	}

	public FotoPedido getDefaultFotoPedidoFilter(Pedido pedido) {
		FotoPedido fotoPedidoFilter = new FotoPedido();
		fotoPedidoFilter.cdEmpresa = pedido.cdEmpresa;
		fotoPedidoFilter.cdRepresentante = pedido.cdRepresentante;
		fotoPedidoFilter.nuPedido = pedido.nuPedido;
		fotoPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
		return fotoPedidoFilter;
	}
    
	public Vector getImageListToSync() throws SQLException {
		Vector imageList = new Vector();
		Vector fotoPedidoList = FotoPedidoDbxDao.getInstance().findAllAlterados();
		for (int i = 0; i < fotoPedidoList.size(); i++) {
			FotoPedido fotoPedido = (FotoPedido) fotoPedidoList.items[i];
			if (!fotoPedido.isFotoPedidoEnviadaServidor()) {
				imageList.addElement(fotoPedido.nmFoto);
			}
		}
		return imageList;
	}

	public void atualizaFotoAposEnvio(Vector nmFotoList, String flEnviadoServidor) throws SQLException {
		int size = nmFotoList.size();
		for (int i = 0; i < size; i++) {
			String nmFoto = (String) nmFotoList.items[i];
			FotoPedido fotoPedidoFilter = new FotoPedido();
			fotoPedidoFilter.nmFoto = nmFoto;
			Vector fotoPedidoList = findAllByExample(fotoPedidoFilter);
			if (ValueUtil.isNotEmpty(fotoPedidoList)) {
				FotoPedido fotoPedido = (FotoPedido) fotoPedidoList.items[0];
				fotoPedido.flEnviadoServidor = flEnviadoServidor;
				try {
					update(fotoPedido);
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			}
		}
	}

	public void atualizaFotosPedido(Pedido pedido) throws SQLException {
		if (ValueUtil.isNotEmpty(pedido.fotoList)) {
			for (int i = 0; i < pedido.fotoList.size(); i++) {
				FotoPedido fotoPedido = (FotoPedido) pedido.fotoList.items[i];
				String nmFoto = fotoPedido.nmFoto;
				delete(fotoPedido);
				fotoPedido.nuPedido = pedido.nuPedido;
				fotoPedido.flOrigemPedido = pedido.flOrigemPedido;
				fotoPedido.nmFoto = fotoPedido.nmFoto.replace("null", pedido.nuPedido); 
				insert(fotoPedido);
				try {
					FileUtil.renameFile(FotoPedido.getPathImg() + "/" + nmFoto, FotoPedido.getPathImg() + "/" + fotoPedido.nmFoto);
				} catch (Throwable e) {
				}
			}
		}
	}

	public void deleteFotos(Pedido pedido) throws SQLException {
		for (int i = 0; i < pedido.fotoList.size(); i++) {
			FotoPedido fotoPedido = (FotoPedido) pedido.fotoList.items[i];
			excluiFotoPedido(pedido, fotoPedido.nmFoto, false);
		}
		pedido.fotoList.setSize(0);
	}

	public void deleteFotoPedido(Pedido pedido) throws SQLException {
		FotoPedido fotoPedidoFilter = new FotoPedido();
		fotoPedidoFilter.cdEmpresa = pedido.cdEmpresa;
		fotoPedidoFilter.cdRepresentante = pedido.cdRepresentante;
		fotoPedidoFilter.nuPedido = pedido.nuPedido;
		fotoPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
		Vector fotoPedidoList = FotoPedidoService.getInstance().findAllByExample(fotoPedidoFilter);
		if (ValueUtil.isNotEmpty(fotoPedidoList)) {
			pedido.fotoList = fotoPedidoList;
			deleteFotos(pedido);
		}
	}

}
