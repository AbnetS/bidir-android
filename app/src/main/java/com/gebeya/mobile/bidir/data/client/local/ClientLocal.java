package com.gebeya.mobile.bidir.data.client.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientComparator;
import com.gebeya.mobile.bidir.data.client.Client_;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Local implementation for the {@link ClientLocalSource} interface contract.
 */
public class ClientLocal extends BaseLocalSource implements ClientLocalSource {

    private final Box<Client> box;

    public ClientLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(Client.class);
    }

    @Override
    public Observable<Data<Client>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<Client>> get(@NonNull String id) {
        final List<Client> clients = box.find(Client_._id, id);
        return Observable.just(new Data<>(clients.isEmpty() ? null : clients.get(0)));
    }

    @Override
    public Observable<Data<Client>> get(int position) {
        final List<Client> clients = box.getAll();
        return Observable.just(new Data<>(clients.isEmpty() ? null : clients.get(position)));
    }

    @Override
    public Observable<List<Client>> getAll(@NonNull String id) {
        final List<Client> clients = box.query()
                .equal(Client_.createdBy, id)
                .and()
                .equal(Client_.groupId, 0)
                .and()
                .equal(Client_.forGroup, false)
                .build()
                .find();
        Collections.sort(clients, new ClientComparator());
        return Observable.just(clients);
    }


    @Override
    public Observable<List<Client>> getByStatus(@NonNull String status) {
        final List<Client> clients = box.query()
                .equal(Client_.status, status)
                .sort(new ClientComparator())
                .build()
                .find();
        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getClientWithACAT() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.ACAT_AUTHORIZED)
                .or()
                .equal(Client_.status, ClientParser.ACAT_IN_PROGRESS)
                .or()
                .equal(Client_.status, ClientParser.ACAT_SUBMITTED)
                .or()
                .equal(Client_.status, ClientParser.ACAT_RESUBMITTED)
                .or()
                .equal(Client_.status, ClientParser.ACAT_DECLINED_FOR_REVIEW)
                .or()
                .equal(Client_.status, ClientParser.LOAN_APPLICATION_ACCEPTED)
                .and()
                .equal(Client_.forGroup, false)
                .and()
                .equal(Client_.groupId, 0)
                .build()
                .find();
        Collections.sort(clients, new ClientComparator());
        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getClientForGroup() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.LOAN_PAID)
                .build()
                .find();
        Collections.sort(clients, new ClientComparator());

        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getNewClients() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.NEW)
                .and()
                .equal(Client_.forGroup, false)
                .and()
                .equal(Client_.groupId, 0)
                .build()
                .find();

        Collections.sort(clients, new ClientComparator());

        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getScreeningClients() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.SCREENING_IN_PROGRESS)
                .or()
                .equal(Client_.status, ClientParser.ELIGIBLE)
                .or()
                .equal(Client_.status, ClientParser.INELIGIBLE)
                .and()
                .equal(Client_.forGroup, false)
                .and()
                .equal(Client_.groupId, 0)
                .build()
                .find();

        Collections.sort(clients, new ClientComparator());

        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getLoanClients() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.LOAN_APPLICATION_NEW)
                .or()
                .equal(Client_.status, ClientParser.LOAN_APPLICATION_IN_PROGRESS)
                .or()
                .equal(Client_.status, ClientParser.LOAN_APPLICATION_ACCEPTED)
                .or()
                .equal(Client_.status, ClientParser.LOAN_APPLICATION_DECLINED)
                .or()
                .equal(Client_.status, ClientParser.LOAN_APPLICATION_REJECTED)
                .and()
                .equal(Client_.forGroup, false)
                .and()
                .equal(Client_.groupId, 0)
                .build()
                .find();

        Collections.sort(clients, new ClientComparator());

        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getACATClients() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.ACAT_NEW)
                .or()
                .equal(Client_.status, ClientParser.ACAT_IN_PROGRESS)
                .or()
                .equal(Client_.status, ClientParser.ACAT_SUBMITTED)
                .or()
                .equal(Client_.status, ClientParser.ACAT_AUTHORIZED)
                .or()
                .equal(Client_.status, ClientParser.ACAT_RESUBMITTED)
                .or()
                .equal(Client_.status, ClientParser.ACAT_DECLINED_FOR_REVIEW)
                .and()
                .equal(Client_.forGroup, false)
                .and()
                .equal(Client_.groupId, 0)
                .build()
                .find();

        Collections.sort(clients, new ClientComparator());

        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getLoanGrantedClients() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.LOAN_GRANTED)
                .and()
                .equal(Client_.forGroup, false)
                .and()
                .equal(Client_.groupId, 0)
                .sort(new ClientComparator())
                .build()
                .find();

        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getLoanPaidClients() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.LOAN_PAID)
                .and()
                .equal(Client_.forGroup, false)
                .and()
                .equal(Client_.groupId, 0)
                .sort(new ClientComparator())
                .build()
                .find();

        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getNewACATClients() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.ACAT_NEW)
                .and()
                .equal(Client_.groupId, 0)
                .build()
                .find();

        Collections.sort(clients, new ClientComparator());

        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getInprogressACATClients() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.ACAT_IN_PROGRESS)
                .and()
                .equal(Client_.groupId, 0)
                .build()
                .find();

        Collections.sort(clients, new ClientComparator());

        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getSubmittedACATClients() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.ACAT_SUBMITTED)
                .and()
                .equal(Client_.groupId, 0)
                .build()
                .find();

        Collections.sort(clients, new ClientComparator());

        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getResubmittedACATClients() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.ACAT_RESUBMITTED)
                .and()
                .equal(Client_.groupId, 0)
                .build()
                .find();

        Collections.sort(clients, new ClientComparator());

        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getApprovedACATClients() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.ACAT_AUTHORIZED)
                .and()
                .equal(Client_.groupId, 0)
                .build()
                .find();

        Collections.sort(clients, new ClientComparator());

        return Observable.just(clients);
    }

    @Override
    public Observable<List<Client>> getDeclinedACATClients() {
        final List<Client> clients = box.query()
                .equal(Client_.status, ClientParser.ACAT_DECLINED_FOR_REVIEW)
                .and()
                .equal(Client_.groupId, 0)
                .build()
                .find();

        Collections.sort(clients, new ClientComparator());

        return Observable.just(clients);
    }

    @Override
    public Observable<Client> markForUpload(@NonNull Client client) {
        client.modified = true;
        client.uploaded = false;
        client.updatedAt = new DateTime();

        box.put(client);

        return Observable.just(client);
    }

    @Override
    public Completable markForCreation(@NonNull Client client) {
        final List<Client> clients = box.query()
                .equal(Client_.idNumber, client.idNumber)
                .or()
                .equal(Client_.phoneNumber, client.phoneNumber)
                .build().find();

        if (!clients.isEmpty()) {
            return Completable.error(new Throwable(ApiErrors.REGISTER_PART_ALREADY_EXISTS));
        }

        client.pendingCreation = true;
        client.updatedAt = new DateTime();

        box.put(client);

        return Completable.complete();
    }

    @Override
    public Observable<Boolean> hasUnSyncedData() {
        final List<Client> modified = getAllModifiedNonUploaded();
        if (!modified.isEmpty()) return Observable.just(true);

        final List<Client> created = getAllPendingCreationNonUploaded();
        if (!created.isEmpty()) return Observable.just(true);

        return Observable.just(false);
    }

    @Override
    public List<Client> getAllModifiedNonUploaded() {
        return box.query()
                .equal(Client_.modified, true)
                .and()
                .equal(Client_.uploaded, false)
                .and()
                .equal(Client_.pendingCreation, false)
                .build()
                .find();
    }

    @Override
    public List<Client> getAllPendingCreationNonUploaded() {
        return box.query()
                .equal(Client_.pendingCreation, true)
                .build()
                .find();
    }

    @Override
    public Completable updateWithLocalIds(List<Client> remoteClients) {
        final int length = remoteClients.size();
        for(int i = 0; i < length; i++) {
            Client remoteClient = remoteClients.get(i);
            List<Client> localClients = box.find(Client_._id, remoteClient._id);
            if (!localClients.isEmpty()) {
                Client localClient = localClients.get(0);
                remoteClient.id = localClient.id;
            }
        }

        return Completable.complete();
    }

    @Override
    public Observable<Client> put(@NonNull Client client) {
        box.query()
                .equal(Client_._id, client._id)
                .or()
                .equal(Client_.idNumber, client.idNumber)
                .build().remove();

        box.put(client);
        return Observable.just(client);
    }

    @Override
    public Observable<List<Client>> putAll(@NonNull List<Client> clients) {
        box.removeAll();
        box.put(clients);

        return Observable.just(clients);
    }

    @Override
    public Completable remove(@NonNull String id) {
        box.query().equal(Client_._id, id).build().remove();
        return Completable.complete();
    }

    @Override
    public Completable remove(int position) {
        final List<Client> clients = box.getAll();
        if (!clients.isEmpty()) box.remove(clients.get(position));
        return Completable.complete();
    }

    @Override
    public Completable removeAll() {
        box.removeAll();
        return Completable.complete();
    }

    @Override
    public int size() {
        return (int) box.count();
    }
}