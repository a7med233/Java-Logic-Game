package swapstones.model;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;


public class Stopwatch {

    private final LongProperty seconds;
    private final StringProperty hhmmss;
    private final Timeline timeline;

    public Stopwatch() {
        seconds = new SimpleLongProperty();
        hhmmss = new SimpleStringProperty();
        timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> seconds.set(seconds.get() + 1)),
                new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        hhmmss.bind(Bindings.createStringBinding(() -> DurationFormatUtils.formatDuration(seconds.get() * 1000, "HH:mm:ss"), seconds));
    }

    public LongProperty secondsProperty() {
        return seconds;
    }

    public StringProperty hhmmssProperty() {
        return hhmmss;
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.pause();
    }

    public void reset() {
/*        if (timeline.getStatus() != Animation.Status.PAUSED) {
            throw new IllegalStateException();
        }*/
        seconds.set(0);
    }

    public Animation.Status getStatus() {
        return timeline.getStatus();
    }

}
