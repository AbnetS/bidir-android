package com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.downloader;

import com.gebeya.mobile.bidir.data.acatform.ACATForm;
import com.gebeya.mobile.bidir.data.acatform.local.ACATFormLocalSource;
import com.gebeya.mobile.bidir.data.acatform.remote.ACATFormRemoteSource;
import com.gebeya.mobile.bidir.data.acatform.remote.ACATFormResponse;
import com.gebeya.mobile.bidir.data.acatform.remote.PaginatedACATFormResponse;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.crop.local.CropLocalSource;
import com.gebeya.mobile.bidir.data.pagination.remote.PageResponse;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

/**
 * Concrete implementation for the {@link BaseDataDownloader} class, specific to a {@link ACATForm}
 * data.
 */
public class ACATFormDataDownloader extends BaseDataDownloader {

    @Inject ACATFormRemoteSource acatFormRemote;
    @Inject ACATFormLocalSource acatFormLocal;
    @Inject CropLocalSource cropLocal;

    public ACATFormDataDownloader(@NonNull String label, @NonNull Service type) {
        super(label, type);
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public void downloadPaginationData() throws Exception {
        d("Downloading pagination data...");
        final PageResponse response = acatFormRemote.downloadPageData();

        final int totalPages = response.totalPages;
        final int currentPage = response.currentPage;

        d("-> Total pages: " + totalPages);
        d("-> Current page: " + currentPage);

        d("Creating pagination data...");
        manipulator.create(response.type, totalPages);

        notifyCallbacksPaginationDataDownloaded();
    }

    @Override
    public void download() throws Exception {
        final int currentPage = manipulator.getCurrentPage() + 1;
        final int totalPages = manipulator.getTotalPages();

        d("Downloading page %d of %d", currentPage, totalPages);

        final PaginatedACATFormResponse paginatedResponse = acatFormRemote.getAllPaginated(
                currentPage
        );
        for (final ACATFormResponse response : paginatedResponse.responses) {
            acatFormLocal.put(response.acatForm);
            cropLocal.put(response.crop);
        }
        d("-> Data saved");

        final int updatedTotalPages = paginatedResponse.pageResponse.totalPages;
        manipulator.updateTotalPages(updatedTotalPages);

        manipulator.next();

        pageLocal.update(manipulator.getPage());

        notifyCallbacks();

        d("-> Page information saved");
    }

    @Override
    public void start() {
        super.start();
        try {
            while (manipulator.hasNext()) {
                download();
            }
        } catch (Exception e) {
            e("Downloading paginated data failed:");
            e.printStackTrace();
        }
        d("Download stopped");
    }
}
