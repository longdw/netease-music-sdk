package com.netease.android.extension.servicekeeper.id;

import androidx.annotation.NonNull;
import com.netease.android.extension.ext.ObjectExt;
import com.netease.android.extension.ext.StringExt;
import com.netease.android.extension.servicekeeper.keeper.IServiceKeeper;

public abstract class AbstractServiceUniqueId<ServiceKeeper extends IServiceKeeper> implements IServiceUniqueId<ServiceKeeper> {
    @NonNull
    private String name;
    private ServiceUniqueIdScope expectServiceUniqueIdScope;

    public AbstractServiceUniqueId() {
        this(StringExt.randomUUIDString());
    }

    public AbstractServiceUniqueId(@NonNull String name) {
        this.expectServiceUniqueIdScope = ServiceUniqueIdScope.LOCAL;
        this.name = name;
    }

    public AbstractServiceUniqueId(@NonNull String name, ServiceUniqueIdScope expectServiceUniqueIdScope) {
        this.expectServiceUniqueIdScope = ServiceUniqueIdScope.LOCAL;
        this.name = name;
        this.expectServiceUniqueIdScope = expectServiceUniqueIdScope;
    }

    public ServiceUniqueIdScope getExpectServiceUniqueIdScope() {
        return this.expectServiceUniqueIdScope;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String toString() {
        return "UniqueId{" + this.name + "}";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbstractServiceUniqueId that = (AbstractServiceUniqueId)o;
            return ObjectExt.isEquals(this.name, that.name);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return ObjectExt.hash(new Object[]{this.name});
    }
}