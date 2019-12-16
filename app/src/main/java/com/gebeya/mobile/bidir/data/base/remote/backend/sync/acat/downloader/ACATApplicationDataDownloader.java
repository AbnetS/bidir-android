package com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.downloader;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationResponse;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationRemote;
import com.gebeya.mobile.bidir.data.acatapplication.remote.PaginatedACATApplicationResponse;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.pagination.remote.PageResponse;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

/**
 * Concrete implementation for the {@link BaseDataDownloader} class, specific to a
 * {@link ACATApplication} object and its instances.
 */
public class ACATApplicationDataDownloader extends BaseDataDownloader {

    @Inject ACATApplicationRemote applicationRemote;
    @Inject ACATApplicationRepositorySource applicationRepo;

    public ACATApplicationDataDownloader(String label, Service type) {
        super(label, type);
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }

    @Override
    public void downloadPaginationData() throws Exception {
        d("Download pagination data...");
        final PageResponse response = applicationRemote.downloadPageData();

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

        final PaginatedACATApplicationResponse paginatedResponse =
                applicationRemote.getAllPaginated(currentPage);

        for (final ACATApplicationResponse response : paginatedResponse.applicationResponses) {
            applicationRepo.saveACATCostComponentsLocally(response);
            applicationRepo.saveACATRevenueComponentsLocally(response);
            applicationRepo.saveACATApplicationSync(response);
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
