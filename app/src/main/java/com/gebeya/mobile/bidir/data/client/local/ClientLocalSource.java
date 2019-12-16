package com.gebeya.mobile.bidir.data.client.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.DeletableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadOne;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.client.Client;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Interface contract for managing the local Clients on the device
 */
public interface ClientLocalSource extends
        ReadOne<Client>,
        WritableSource<Client>,
        DeletableSource,
        ReadSize {

    /**
     * Return a list of all the {@link Client} objects only created by the given userId.
     */
    Observable<List<Client>> getAll(@NonNull String userId);

    /**
     * Get a list of {@link Client} with the given status.
     *
     * @param status Status to use as the query parameter.
     * @return Observable list of the found Client data.
     */
    Observable<List<Client>> getByStatus(@NonNull String status);

    /**
     * Get a list of {@link Client} with ACAT or to be created.
     * @return Observable list of the found client data.
     */

    Observable<List<Client>> getClientWithACAT();
    /**
     * Marks the client as modified and prepare it for upload.
     *
     * @param client The client to mark.
     * @return Observable of the same marked client.
     */
    Observable<Client> markForUpload(@NonNull Client client);

    /**
     * Creates a client offline for later upload, when there is connectivity.
     *
     * @param client Temporary client used to store the data.
     * @return Completable indicating that the action has finished.
     */
    Completable markForCreation(@NonNull Client client);

    /**
     * Returns an observable that represents whether there is data that needs to be synced or not.
     *
     * @return Observable containing the boolean result
     */
    Observable<Boolean> hasUnSyncedData();

    /**
     * Get a list of {@link Client}s that have been modified, and need to be uploaded.
     *
     * @return List of the found Clients.
     */
    List<Client> getAllModifiedNonUploaded();

    /**
     * Get a list of {@link Client}s that have been created but still need to be uploaded.
     *
     * @return List of the found clients.
     */
    List<Client> getAllPendingCreationNonUploaded();

    /**
     * Updates the given list of remote Clients with the ID values of the local Clients where there
     * are any matches found.
     *
     * This is necessary in order to avoid ObjectBox inserting the provided remote Clients as if
     * they were completely new Client records, creating duplicate records.
     */
    Completable updateWithLocalIds(List<Client> remoteClients);

    /**
     * Get all of the clients that are eligible to be added to a group application.
     *
     * @return List of Clients with status Loan Granted and new.
     */
    Observable<List<Client>> getClientForGroup();

    /**
     * Get all new Clients.
     * @return
     */
    Observable<List<Client>> getNewClients();

    /**
     * Get Clients under screening stage.
     * @return
     */
    Observable<List<Client>> getScreeningClients();

    /**
     * Get Clients under loan application state.
     * @return
     */
    Observable<List<Client>> getLoanClients();

    /**
     * Get Clients under A-CAT state.
     * @return
     */
    Observable<List<Client>> getACATClients();

    /**
     * Get Loan granted Clients
     * @return
     */
    Observable<List<Client>> getLoanGrantedClients();

    /**
     * Get Clients that paid their loan.
     * @return
     */
    Observable<List<Client>> getLoanPaidClients();

    /**
     * Get Clients with new ACATs.
     *
     * @return
     */
    Observable<List<Client>> getNewACATClients();

    /**
     * Get Clients with Inprogress ACATs.
     *
     * @return
     */
    Observable<List<Client>> getInprogressACATClients();

    /**
     * Get Clients with submitted ACATs.
     *
     * @return
     */
    Observable<List<Client>> getSubmittedACATClients();

    /**
     * Get Clients with resubmitted ACATs.
     *
     * @return
     */
    Observable<List<Client>> getResubmittedACATClients();

    /**
     * Get Clients with approved ACATs.
     *
     * @return
     */
    Observable<List<Client>> getApprovedACATClients();

    /**
     * Get Clients with declined ACATs.
     *
     * @return
     */
    Observable<List<Client>> getDeclinedACATClients();

}