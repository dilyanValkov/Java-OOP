package harpoonDiver.repositories;
import harpoonDiver.models.divingSite.DivingSite;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
public class DivingSiteRepository implements Repository <DivingSite>{
    private Collection<DivingSite> sites;

    public DivingSiteRepository() {
        this.sites = new ArrayList<>();
    }

    @Override
    public Collection<DivingSite> getCollection() {
        return Collections.unmodifiableCollection(this.sites);
    }

    @Override
    public void add(DivingSite site) {
        this.sites.add(site);
    }

    @Override
    public boolean remove(DivingSite site) {
        return this.sites.remove(site);
    }

    @Override
    public DivingSite byName(String name) {
        return this.sites.stream().filter(site ->
                site.getName().equals(name)).findFirst().orElse(null);
    }
}
