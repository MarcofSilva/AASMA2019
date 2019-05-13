package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;
import java.util.*;
import java.util.List;

public class IntersectionManager {

    List<List<Point>> allPaths = new ArrayList<>();
    Map<String, Integer> keysToIndexes = new HashMap<>();
    Map<String, Car> keysToCars = new HashMap<>();
    Map<String, Point> keyToAction = new HashMap<>();
    int priority = 0;

    public IntersectionManager(){

    }

    public void reset(){
        allPaths.clear();
        keysToIndexes.clear();
        keysToCars.clear();
        keyToAction.clear();
        priority++;
    }
    public void updatePath(String id, List<Point> path){
        int index = Integer.valueOf(keysToIndexes.get(id));
        List<Point> updatedPath = new ArrayList<>();
        updatedPath.addAll(path);
        allPaths.set(index,updatedPath);
    }

    public String signInIntersection(List<Point> path, Car car){
        List<Point> pathToAdd = new ArrayList<>();
        pathToAdd.addAll(path);
        int index = -1;
        for(int i = 0; i < allPaths.size() ; i++){
            if(allPaths.get(i) == null){
                index = i;
                allPaths.set(index, pathToAdd);
                break;
            }
        }
        if(index == -1){
            index = allPaths.size();
            allPaths.add(pathToAdd);
        }
        priority++;
        String key = String.valueOf(priority);
        keysToIndexes.put(key, index);
        keysToCars.put(key, car);
        return key;
    }

    public void resolve(){
        keyToAction.clear();
        ArrayList<String> prioritiesStr = new ArrayList<>(keysToIndexes.keySet());
        ArrayList<Integer> prioritiesOrdered = new ArrayList<>();
        for(String str : prioritiesStr){
            prioritiesOrdered.add(Integer.valueOf(str));
        }
        Collections.sort(prioritiesOrdered);
        ArrayList<Integer> indexesByPriority = new ArrayList<>();
        for(Integer index : prioritiesOrdered){
            indexesByPriority.add(keysToIndexes.get(String.valueOf(index)));
        }
        ArrayList<Point> pointsToConsider = new ArrayList<>();
        ArrayList<Point> nextsToConsider = new ArrayList<>();
        for(Integer index : indexesByPriority){
            pointsToConsider.add(allPaths.get(index).get(0));
            if(allPaths.get(index).size() == 1){
                nextsToConsider.add(null);
            }
            else {
                nextsToConsider.add(allPaths.get(index).get(1));
            }
        }
        boolean breakLoop = false;
        for(int i = 0; i < prioritiesOrdered.size() ; i++){
            breakLoop = false;
            String id = String.valueOf(prioritiesOrdered.get(i));
            Point pointToGo = pointsToConsider.get(i);
            Point pointToGoNext = nextsToConsider.get(i);

            if(keyToAction.containsValue(pointToGo)){
                Point aux = keysToCars.get(id).point;
                keyToAction.put(id, new Point(aux.x, aux.y));
                breakLoop = true;
            }
            if(breakLoop){
                continue;
            }
            for(int j = 0; j < i; j++){
                if(comparePoints(pointToGo, pointsToConsider.get(j))){
                    Point aux = keysToCars.get(id).point;
                    keyToAction.put(id, new Point(aux.x, aux.y));
                    breakLoop = true;
                    break;
                }
            }
            if(breakLoop){
                continue;
            }
            for(int j = 0; j < i; j++){
                Point next = nextsToConsider.get(j);
                if(next == null || pointToGoNext == null){
                    continue;
                }
                if(comparePoints(pointToGoNext, next)){
                    Point aux = keysToCars.get(id).point;
                    keyToAction.put(id, new Point(aux.x, aux.y));
                    break;
                }
            }
            if(breakLoop){
                continue;
            }
            for(int j = 0; j < i; j++){
                Point next = nextsToConsider.get(j);
                if(next == null){
                    continue;
                }
                if(comparePoints(pointToGo, next)){
                    Point aux = keysToCars.get(id).point;
                    keyToAction.put(id, new Point(aux.x, aux.y));
                    breakLoop = true;
                    break;
                }
            }
            if(breakLoop){
                continue;
            }
            keyToAction.put(id, pointToGo);
        }
    }
    public boolean comparePoints(Point p1, Point p2){
        if(p1.x != p2.x){
            return false;
        }
        return p1.y == p2.y;
    }
    public Point getAction(String id){
        Point answer = keyToAction.get(id);
        //TODO
        if(!keyToAction.containsKey(id)){
            System.out.print("");
        }
        Integer index = keysToIndexes.get(id);

        if(comparePoints(answer, allPaths.get(index).get(0))){
            allPaths.get(index).remove(0);
        }
        if(allPaths.get(index).size() == 0){
            allPaths.set(index,null);
            keysToIndexes.remove(id);
            keysToCars.remove(id);
        }
        return answer;
    }
}
