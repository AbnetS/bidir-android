package com.gebeya.mobile.bidir.ui.summary;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.repo.CropACATRepoSource;
import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Presenter implementation for the summary screen.
 */
public class SummaryPresenter implements SummaryContract.Presenter {

    private SummaryContract.View view;

    private final String clientId;
    private final String cropACATId;
    private CropACAT cropACAT;
    @Nullable private Client client;
    private final List<SummaryMenuItem> items;
    public static int selected;
    private List<Permission> permissions;

    @Inject ClientLocalSource clientLocal;
    @Inject CropACATLocalSource cropACATLocal;
    @Inject SchedulersProvider schedulers;
    @Inject CropACATRepoSource cropACATRepo;
    @Inject PermissionLocalSource permissionLocal;

    public SummaryPresenter(@NonNull String cropACATId, @NonNull String clientId) {
        this.cropACATId = cropACATId;
        this.clientId = clientId;
        items = new ArrayList<>();
        permissions = new ArrayList<>();
        selected = 2;
        Tooth.inject(this, Scopes.SCOPE_STATES);
    }

    @Override
    @SuppressLint("CheckResult")
    public void start() {
        buildMenu();
        view.loadMenu();

        clientLocal.get(clientId)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                            client = data.get();
                            loadClientInfo();
                        },
                        Throwable::printStackTrace
                );

        cropACATLocal.get(cropACATId)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                            cropACAT = data.get();
                            loadCropInfo();
                        },
                        Throwable::printStackTrace
                );

        permissionLocal.getByEntity(PermissionHelper.ENTITY_ACAT_PROCESSOR)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    permissions.clear();
                    permissions.addAll(list);

                    applyPermissions();
                });
    }

    private void applyPermissions() {
        final boolean canAuthorize = hasPermission(PermissionHelper.OPERATION_AUTHORIZE);

        // TODO: Refactor this.
        view.toggleApproveButton(false);
        view.toggleDeclineButton(false);
    }

    private boolean hasPermission(@NonNull String operation) {
        final int length = permissions.size();
        for (int i = 0; i < length; i++) {
            final Permission permission = permissions.get(i);
            if (permission.operation.equals(operation)) {
                return true;
            }
        }
        return false;
    }

    private void loadCropInfo() {
        if (cropACAT == null) return;

        view.setCropName(cropACAT.cropName);
        view.setCropFirstExpenseMonth(cropACAT.firstExpenseMonth);
        view.setCroppingArea(cropACAT.croppingArea);
    }

    private void loadClientInfo() {
        if (client == null) return;

        view.setTitle(Utils.formatName(
                client.firstName,
                client.surname,
                client.lastName
        ));
    }

    private void buildMenu() {
        items.clear();

        items.add(new SummaryMenuItem(SummaryMenuItem.TYPE_TITLE, SummaryMenuItem.Label.COST_ESTIMATION));
        items.add(new SummaryMenuItem(SummaryMenuItem.TYPE_SUBTITLE, SummaryMenuItem.Label.INPUTS_AND_ACTIVITIES_COST));
        items.add(new SummaryMenuItem(SummaryMenuItem.TYPE_PARENT, SummaryMenuItem.Label.INPUTS));
        items.add(new SummaryMenuItem(SummaryMenuItem.TYPE_CHILD, SummaryMenuItem.Label.SEEDS));
        items.add(new SummaryMenuItem(SummaryMenuItem.TYPE_CHILD, SummaryMenuItem.Label.FERTILIZERS));
        items.add(new SummaryMenuItem(SummaryMenuItem.TYPE_CHILD, SummaryMenuItem.Label.CHEMICALS));
        items.add(new SummaryMenuItem(SummaryMenuItem.TYPE_PARENT, SummaryMenuItem.Label.LABOR_COST));
        items.add(new SummaryMenuItem(SummaryMenuItem.TYPE_PARENT, SummaryMenuItem.Label.OTHER_COSTS));
        items.add(new SummaryMenuItem(SummaryMenuItem.TYPE_TITLE, SummaryMenuItem.Label.ESTIMATED_YIELD));
        items.add(new SummaryMenuItem(SummaryMenuItem.TYPE_PARENT, SummaryMenuItem.Label.PROBABLE_YIELD));
        items.add(new SummaryMenuItem(SummaryMenuItem.TYPE_PARENT, SummaryMenuItem.Label.MAXIMUM_YIELD));
        items.add(new SummaryMenuItem(SummaryMenuItem.TYPE_PARENT, SummaryMenuItem.Label.MINIMUM_YIELD));

        loadSummaryData(items.get(2));
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryContract.MenuItemView holder, int position) {
        final SummaryMenuItem item = items.get(position);
        holder.setLabel(item.type, item.label);
        holder.setSelected(position == selected);
    }

    @Override
    public void onMenuItemSelected(int position) {
        if (position < 0) return;       // TODO: Debug and fix this

        final SummaryMenuItem item = items.get(position);
        final int type = item.type;
        if (type == SummaryMenuItem.TYPE_TITLE || type == SummaryMenuItem.TYPE_SUBTITLE) return;
        selected = position;
        view.refreshMenu();
        loadSummaryData(item);
    }

    private void loadSummaryData(@NonNull SummaryMenuItem item) {
        final SummaryMenuItem.Label label = item.label;

        switch (label) {
            case INPUTS:
                view.loadInputs(clientId, cropACATId);
                break;
            case PROBABLE_YIELD:
            case MAXIMUM_YIELD:
            case MINIMUM_YIELD:
                view.loadYields(label, clientId, cropACATId);
                break;
            case SEEDS:
            case FERTILIZERS:
            case CHEMICALS:
            case LABOR_COST:
            case OTHER_COSTS:
                view.loadCosts(label, clientId, cropACATId);
                break;
        }

    }

    @Override
    public int itemCount() {
        return items.size();
    }

    @Override
    public int getType(int position) {
        return items.get(position).type;
    }

    @Override
    public void onBackButtonPressed() {
        view.close();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onApproveClicked() {
        view.showUpdatingProgress();
        cropACATRepo.approveCropACAT(cropACAT._id, cropACAT.acatApplicationID)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
                    view.showUpdateSuccessfulMessage();
                    view.hideUpdatingProgress();
                    view.close();
                }, this::onUpdateFailed); // TODO: Handle error and use result.
    }

    public void onUpdateFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        final String message = ApiErrors.LOAN_APPROVED_UPDATE_GENERIC_ERROR;

        view.hideUpdatingProgress();
        view.showError(Result.createError(message, error));
    }

    @Override
    public void onDeclineClicked() {
        view.openDeclineDialog();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onDeclineReturned(@NonNull String remark, boolean isFinal) {
        view.showUpdatingProgress();
        cropACATRepo.declineCropACAT(cropACAT._id, cropACAT.acatApplicationID)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
                    view.showUpdateSuccessfulMessage();
                    view.hideUpdatingProgress();
                    view.close();
                }, this::onUpdateFailed); // TODO: Handle error and us e result.
    }

    @Override
    public void attachView(SummaryContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public SummaryContract.View getView() {
        return view;
    }
}
