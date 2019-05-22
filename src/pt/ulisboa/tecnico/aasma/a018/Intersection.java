package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Intersection {

    Map<String, Point> exitsByDirection = new HashMap<>();
    List<String> possibleDirections;

    public Intersection(Point location, int direction, List<String> exits){
        possibleDirections = exits;

        for(String exit : exits){
            switch(exit){
                case "F":
                    exitsByDirection.put("F", calcExitF(location, direction));
                    break;
                case "R":
                    exitsByDirection.put("R", calcExitR(location, direction));
                    break;
                default:
                    exitsByDirection.put("L", calcExitL(location, direction));
            }
        }
    }

    private Point calcExitF(Point location, int direction){
        switch(direction){
            case 0:
                return new Point(location.x + 3, location.y);
            case 90:
                return new Point(location.x, location.y - 3);
            case 180:
                return new Point(location.x - 3, location.y);
            default:
                return new Point(location.x, location.y + 3);
        }
    }

    private Point calcExitR(Point location, int direction){
        switch(direction){
            case 0:
                return new Point(location.x + 1, location.y - 1);
            case 90:
                return new Point(location.x - 1, location.y - 1);
            case 180:
                return new Point(location.x - 1, location.y + 1);
            default:
                return new Point(location.x + 1, location.y + 1);
        }
    }

    private Point calcExitL(Point location, int direction){
        switch(direction){
            case 0:
                return new Point(location.x + 2, location.y + 2);
            case 90:
                return new Point(location.x + 2, location.y - 2);
            case 180:
                return new Point(location.x - 2, location.y - 2);
            default:
                return new Point(location.x - 2, location.y + 2);
        }
    }

    public ArrayList<Point> calcPathIntersect(Point start, Point end, int direction){
        Point location = new Point(start.x, start.y);
        Point destination = new Point(end.x, end.y);
        ArrayList<Point> path = new ArrayList<>();

        //going horizontal equalize x
        if(direction == 0 || direction == 180){
            while(location.x != destination.x){
                if(location.x > destination.x){
                    location.x--;
                }
                else{
                    location.x++;

                }
                path.add(new Point(location.x, location.y));
            }
            if(location.y != destination.y){
                path.add(new Point(location.x, location.y));
            }
            while(location.y != destination.y){
                if(location.y > destination.y){
                    location.y--;
                }
                else{
                    location.y++;

                }
                path.add(new Point(location.x, location.y));
            }
        }
        //going vertical, equalize y
        else{
            while(location.y != destination.y){
                if(location.y > destination.y){
                    location.y--;
                }
                else{
                    location.y++;

                }
                path.add(new Point(location.x, location.y));
            }
            if(location.x != destination.x){
                path.add(new Point(location.x, location.y));
            }
            while(location.x != destination.x){
                if(location.x > destination.x){
                    location.x--;
                }
                else{
                    location.x++;

                }
                path.add(new Point(location.x, location.y));
            }
        }
        return path;
    }

    public Point getDestination(String direction){
        return exitsByDirection.get(direction);
    }

    public List<String> getPossibleExits(){
        return possibleDirections;
    }

}
