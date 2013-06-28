/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author Администратор
 */
public class Routes {
    int route;
    int proj;

    public int getRoute() {
        return route;
    }

    public int getProj() {
        return proj;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public void setProj(int proj) {
        this.proj = proj;
    }

    @Override
    public String toString() {
        return "Routes{" + "route=" + route + ", proj=" + proj + '}';
    }

    
    
    
}
