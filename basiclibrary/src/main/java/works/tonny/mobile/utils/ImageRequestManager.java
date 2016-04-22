package works.tonny.mobile.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Tonny on 2016/4/22.
 */
public class ImageRequestManager {
    private static ImageRequestManager imageRequestManager = new ImageRequestManager();

    private Timer requestTimer = new Timer();

    private boolean start;

    private List<ImageRequest> requestList = new ArrayList<>();

    private ImageRequest[] imageRequests;

    private int size = 2;

    public static ImageRequestManager getInstance() {
        return imageRequestManager;
    }

    private ImageRequestManager() {
        imageRequests = new ImageRequest[size];
//
//        requestTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        ImageRequest task1 = executingQueue.take();
//                        final ImageRequest.OnRequested onRequested = task1.getOnRequested();
//                        task1.setOnRequested(new ImageRequest.OnRequested() {
//                            @Override
//                            public void execute(File file) {
//                                onRequested.execute(file);
//                                try {
//                                    executingQueue.put(waitingQueue.take());
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                        task1.execute();
//                        ImageRequest task2 = executingQueue.take();
//                        final ImageRequest.OnRequested onRequested2 = task1.getOnRequested();
//                        task2.setOnRequested(new ImageRequest.OnRequested() {
//                            @Override
//                            public void execute(File file) {
//                                onRequested2.execute(file);
//                                try {
//                                    executingQueue.put(waitingQueue.take());
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                        task2.execute();
//                    } catch (InterruptedException e) {
//                        Log.error(e);
//                    }
//                }
//            }
//        }, 1000);
//        syncTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                while (executingQueue.remainingCapacity() > 0) {
//                    try {
//                        executingQueue.put(waitingQueue.take());
//                    } catch (InterruptedException e) {
//                        Log.error(e);
//                    }
//                }
//            }
//        }, 1000);
    }


    private void execute(final int index) {
        ImageRequest task = imageRequests[index];
        final ImageRequest.OnRequested onRequested = task.getOnRequested();
        task.setOnRequested(new ImageRequest.OnRequested() {
            @Override
            public void execute(File file) {
                onRequested.execute(file);
                fillAndExecute(index);
            }
        });
        task.execute();
    }

    private void fillAndExecute(int index) {
        synchronized (requestList) {
            if (!requestList.isEmpty()) {
                imageRequests[index] = requestList.get(0);
                requestList.remove(0);
                ImageRequestManager.this.execute(index);
            } else {
                imageRequests[index] = null;
            }
        }
    }

    public void addTask(ImageRequest task) {
        synchronized (requestList) {
            requestList.add(task);
        }

        if (imageRequests[0] == null) {
            fillAndExecute(0);
            return;
        }
        if (imageRequests[1] == null) {
            fillAndExecute(1);
            return;
        }

    }
}
