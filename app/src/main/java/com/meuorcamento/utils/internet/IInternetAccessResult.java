

package com.meuorcamento.utils.internet;

/**
 *
 * @author Administrador
 */
public interface IInternetAccessResult {
    
    public void onSuccess();

    public void onError(Throwable error);
}
