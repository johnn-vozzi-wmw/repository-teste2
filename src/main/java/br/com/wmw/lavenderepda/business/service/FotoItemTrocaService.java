package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.FotoItemTroca;
import br.com.wmw.lavenderepda.business.domain.FotoPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoItemTrocaDbxDao;
import totalcross.util.Vector;

import java.sql.SQLException;

public class FotoItemTrocaService extends CrudService {
	
	private static FotoItemTrocaService instance;
	
	public static FotoItemTrocaService getInstance() {
		if (instance == null) {
			instance = new FotoItemTrocaService();
		}
		return instance;
	}
	
	@Override
	public void validate(BaseDomain domain) throws SQLException { }

	@Override
	protected CrudDao getCrudDao() {
		return FotoItemTrocaDbxDao.getInstance();
	}

	public FotoItemTroca insereFotoItemTroca(ItemPedido itemTroca, String nmFoto, int cdFotoItemTroca) throws SQLException {
		FotoItemTroca fotoItemTroca = getFotoItemTrocaInstance(itemTroca);
		fotoItemTroca.cdFotoItemTroca = cdFotoItemTroca;
		fotoItemTroca.nmFoto = nmFoto;
		fotoItemTroca.nuTamanho = FotoUtil.getTamanhoFoto(nmFoto, FotoPedido.getPathImg());
		fotoItemTroca.dtModificacao = DateUtil.getCurrentDate();
		insert(fotoItemTroca);
		return fotoItemTroca;
	}

	public void excluiFotoItemTroca(ItemPedido itemTroca, String nmFoto, int cdFotoItemTroca) throws SQLException {
		FotoItemTroca fotoItemTroca = getFotoItemTrocaInstance(itemTroca);
		fotoItemTroca.cdFotoItemTroca = cdFotoItemTroca;
		fotoItemTroca.nmFoto = nmFoto;
		delete(fotoItemTroca);
		FileUtil.deleteFile(FotoPedido.getPathImg() + "/" + nmFoto);
	}

	public boolean isPermiteExcluirFoto(ItemPedido itemTroca, int cdFotoItemTroca) throws SQLException {
		FotoItemTroca fotoItemTrocaFilter = getFotoItemTrocaInstance(itemTroca);
		fotoItemTrocaFilter.cdFotoItemTroca = cdFotoItemTroca;
		FotoItemTroca fotoPedido = (FotoItemTroca) findByRowKey(fotoItemTrocaFilter.getRowKey());
		return BaseDomain.FLTIPOALTERACAO_ALTERADO.equals(fotoPedido.flTipoAlteracao);
	}
	
	public int getSequencialCdFotoItemTroca(ItemPedido pedido) throws SQLException {
		FotoItemTroca fotoItemTroca = getFotoItemTrocaInstance(pedido);
		return countByExample(fotoItemTroca);
	}

	public FotoItemTroca getFotoItemTrocaInstance(ItemPedido itemTroca) {
		FotoItemTroca fotoItemTroca = getFotoItemTrocaPedidoInstance(itemTroca.pedido);
		fotoItemTroca.cdProduto = itemTroca.cdProduto;
		fotoItemTroca.flTipoItemPedido = itemTroca.flTipoItemPedido;
		fotoItemTroca.nuSeqProduto = itemTroca.nuSeqProduto;
		fotoItemTroca.cdUsuarioCriacao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		return fotoItemTroca;
	}

	public FotoItemTroca getFotoItemTrocaPedidoInstance(Pedido pedido) {
		FotoItemTroca fotoItemTroca = new FotoItemTroca();
		fotoItemTroca.cdEmpresa = pedido.cdEmpresa;
		fotoItemTroca.cdRepresentante = pedido.cdRepresentante;
		fotoItemTroca.nuPedido = pedido.nuPedido;
		fotoItemTroca.flOrigemPedido = pedido.flOrigemPedido;
		return fotoItemTroca;
	}

	public String findRSFotoItemTrocaNaoEnviado(Pedido pedido) throws SQLException {
		FotoItemTroca fotoItemTrocaFilter = getFotoItemTrocaPedidoInstance(pedido);
		fotoItemTrocaFilter.flEnviadoServidor = ValueUtil.VALOR_NAO;
		return getCrudDao().findAllByExampleSql(fotoItemTrocaFilter);
	}

	public void atualizaFotoItemTrocaAposEnvio(Vector nmFotoList, String flEnviadoServidor) throws SQLException {
		int size = nmFotoList.size();
		for (int i = 0; i < size; i++) {
			String nmFoto = (String) nmFotoList.items[i];
			FotoItemTroca fotoItemTrocaFilter = new FotoItemTroca();
			fotoItemTrocaFilter.nmFoto = nmFoto;
			Vector fotoItemTrocalist = findAllByExample(fotoItemTrocaFilter);
			if (ValueUtil.isNotEmpty(fotoItemTrocalist)) {
				FotoItemTroca fotoItemTroca = (FotoItemTroca) fotoItemTrocalist.items[0];
				fotoItemTroca.flEnviadoServidor = flEnviadoServidor;
				try {
					update(fotoItemTroca);
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			}
		}
	}

	public Vector getImageListToSync() throws SQLException {
		Vector imageList = new Vector();
		Vector fotoItemTrocaList = FotoItemTrocaDbxDao.getInstance().findAllAlterados();
		for (int i = 0; i < fotoItemTrocaList.size(); i++) {
			FotoItemTroca fotoItemTroca = (FotoItemTroca) fotoItemTrocaList.items[i];
			if (!fotoItemTroca.isFotoPedidoEnviadaServidor()) {
				imageList.addElement(fotoItemTroca.nmFoto);
			}
		}
		return imageList;
	}
	
	public void deleteAllFotosItemTroca(ItemPedido itemTroca) throws SQLException {
		FotoItemTroca fotoItemTrocaFilter = getFotoItemTrocaInstance(itemTroca);
		Vector fotoItemTrocaList = findAllByExample(fotoItemTrocaFilter);
		getCrudDao().deleteAllByExample(fotoItemTrocaFilter);
		String dirFoto = FotoUtil.getPathImg(FotoItemTroca.CLASS_SIMPLE_NAME);
		for (int i = 0; i < fotoItemTrocaList.size(); i++) {
			FotoItemTroca fotoItemTroca = (FotoItemTroca) fotoItemTrocaList.elementAt(i);
			try {
				FileUtil.deleteFile(dirFoto + fotoItemTroca.nmFoto);
			} catch (Exception e) {
				ExceptionUtil.handle(e);
			}
		}
	}

}
