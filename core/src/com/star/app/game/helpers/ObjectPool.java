package com.star.app.game.helpers;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectPool<T extends Poolable> {
    protected List<T> activeList;
    protected List<T> freeList;

    public List<T> getActiveList() {
        return activeList;
    }
    protected abstract T newObject(); // класс-наследник переопределит метод - как создать нужный объект


    public ObjectPool(){
        this.activeList = new ArrayList<T>();
        this.freeList = new ArrayList<T>();
    }

    public void free(int index){
        freeList.add(activeList.remove(index));
    }

    // перекидываем свободный элемент в активный список и возвращаем ссылку на него
    public T getActiveElement(){
        if (freeList.size() ==0){
            freeList.add(newObject());
        }
        T item = freeList.remove(freeList.size()-1);
        activeList.add(item);
        return  item;
    }

    // если элемент "отработал", то он возвращается в "свободный" список
    public void checkPool(){
        for (int i = activeList.size()-1; i > 0 ; i--) {
            if (!activeList.get(i).isActive()){
                free(i);
            }
        }
    }
}
