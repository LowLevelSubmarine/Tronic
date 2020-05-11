package com.tronic.bot.music;

import java.util.Collection;
import java.util.LinkedList;

public class QueueList<T> {

    private final LinkedList<T> content;
    private int pos = -1;

    public QueueList() {
        this.content = new LinkedList<>();
    }

    public QueueList(T t) {
        this.content = new LinkedList<>();
        this.content.add(t);
    }

    public QueueList(Collection<T> collection) {
        this.content = new LinkedList<>(collection);
    }

    public T getCurrent() {
        return this.content.get(this.pos);
    }

    public T getNext() {
        return this.content.get(this.pos + 1);
    }

    public T getPrevious() {
        return this.content.get(this.pos - 1);
    }

    public T get(int pos) {
        return this.content.get(pos);
    }

    public boolean hasNext() {
        return this.content.size() > this.pos + 1;
    }

    public T moveTo(int pos) {
        this.pos = Math.max(Math.min(this.content.size() - 1, pos), 0);
        return getCurrent();
    }

    public T next() {
        this.pos++;
        return getCurrent();
    }

    public T previous() {
        this.pos--;
        return getCurrent();
    }

    public void add(T t) {
        this.content.add(t);
    }

    public void add(T t, int pos) {
        if (isPosAffected(pos)) {
            this.pos++;
        }
        this.content.add(pos, t);
    }

    public void remove(T t) {
        int pos = this.content.indexOf(t);
        if (isPosAffected(pos)) {
            this.pos--;
        }
        this.content.remove(pos);
    }

    public void remove(int pos) {
        if (isPosAffected(pos)) {
            this.pos--;
        }
        this.content.remove(pos);
    }

    public void removeCurrent() {
        this.content.remove(this.pos);
        this.pos--;
    }

    public boolean isEmpty() {
        return this.content.isEmpty();
    }

    public boolean isLast() {
        return this.pos == this.content.size() - 1;
    }

    public boolean isFirst() {
        return this.pos == 0;
    }

    public boolean isPosValid() {
        return -1 < this.pos && this.pos < this.content.size();
    }

    public boolean isIdle() {
        return this.pos == -1;
    }

    public boolean contains(T t) {
        return this.content.contains(t);
    }

    private boolean isPosAffected(int pos) {
        return pos <= this.pos;
    }

}
