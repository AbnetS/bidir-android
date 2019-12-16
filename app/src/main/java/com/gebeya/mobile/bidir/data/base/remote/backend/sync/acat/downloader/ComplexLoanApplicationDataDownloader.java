package com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.downloader;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplicationResponse;
import com.gebeya.mobile.bidir.data.complexloanapplication.local.ComplexLoanApplicationLocalSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationRemote;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationRemoteSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.PaginatedComplexLoanApplicationResponse;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionResponse;
import com.gebeya.mobile.bidir.data.complexquestion.local.ComplexQuestionLocalSource;
import com.gebeya.mobile.bidir.data.pagination.remote.PageResponse;
import com.gebeya.mobile.bidir.data.prerequisite.local.PrerequisiteLocalSource;
import com.gebeya.mobile.bidir.data.section.local.SectionLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

/**
 * Concrete implementation for the {@link BaseDataDownloader} for handling
 * {@link ComplexLoanApplication} data.
 */
public class ComplexLoanApplicationDataDownloader extends BaseDataDownloader {

    @Inject ComplexLoanApplicationRemote loanApplicationRemote;
    @Inject ComplexLoanApplicationLocalSource loanApplicationLocal;

    @Inject ComplexQuestionLocalSource complexQuestionLocal;
    @Inject PrerequisiteLocalSource prerequisiteLocal;
    @Inject SectionLocalSource sectionLocal;

    public ComplexLoanApplicationDataDownloader(@NonNull String label, @NonNull Service type) {
        super(label, type);
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public void downloadPaginationData() throws Exception {
        d("Downloading pagination data...");
        final PageResponse response = loanApplicationRemote.downloadPageData();

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

        final PaginatedComplexLoanApplicationResponse paginatedResponse =
                loanApplicationRemote.getAllPaginated(currentPage);

        final ComplexLoanApplicationResponse response = paginatedResponse.loanApplicationResponse;
        final ComplexLoanApplication application = response.application;
        loanApplicationLocal.put(application);

        for (final ComplexQuestionResponse questionResponse : response.questionResponses) {
            complexQuestionLocal.put(questionResponse.question);
            prerequisiteLocal.putAll(questionResponse.prerequisites);
        }
        sectionLocal.putAll(response.sections);
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
