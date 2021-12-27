package org.zavaglia.advent2021;

import java.util.*;
import java.util.stream.Collectors;

public class Question23 extends Question {

    // there are 11 positions in the top row, 7 of which can be occupied (rather than transited)
    // there are 2 positions in each of the 4 channels.
    // define a room (int), where 0 designates the hallway, and 1-4 designate the rooms left to right
    // For the hallway, the positions 0-10 designate the valid positions from left to right.
    // For each room the positions a number n designates the position in that room, where n indicates the
    // number of steps it would take to reach the hallway.

    public void printTrackBack(Map<PrioritisedGameState, PrioritisedGameState> trackback, PrioritisedGameState state) {
        if (state != null) {
            printTrackBack(trackback, trackback.get(state));
            System.out.println("Cost = " + state.cost);
            System.out.println(state.gs);
            System.out.println("=============");
        }

    }

    public long part1() {

        var start = GameState.parseGameState(getInputText());
        return bfs(start);
    }

    long bfs(GameState start) {
        var pqTrackBack = new HashMap<PrioritisedGameState, PrioritisedGameState>();
        var priorStates = new HashMap<GameState, Integer>();
        var queue = new PriorityQueue<PrioritisedGameState>(Comparator.comparingInt(a -> a.cost));
        queue.add(new PrioritisedGameState(start, 0));
        priorStates.put(start, 0);
        // breadth first search

        var count = 0L;
        while (!queue.isEmpty()) {
            var item = queue.remove();
            count++;
            if (item.gs.done()) {
                System.out.printf("total count: %d\n", count);

                printTrackBack(pqTrackBack, item);
                return item.cost;
            }
            for (var move : item.gs.moves()) {
                var newGs = PrioritisedGameState.create(item.gs, item.cost, move);

                if (priorStates.getOrDefault(newGs.gs, Integer.MAX_VALUE) > newGs.cost) {
                    // ideally we would now prune all paths forward from this redundant (expensive)
                    // gamestate from the queue, this is left TBD
                    queue.add(newGs);
                    priorStates.put(newGs.gs, newGs.cost);
                    pqTrackBack.put(newGs, item);
                }
            }
        }
        System.out.printf("total count: %d\n", count);
        return -1;

    }

    public long part2() {
        var input = getInputText();
        input.add(3, "  #D#C#B#A#");
        input.add(4, "  #D#B#A#C#");
        var start = GameState.parseGameState(getInputText());
        return bfs(start);
    }

    record Position(int room, int roomPosition) {

        Position {
            assert room >= 0 && room <= 4;
            assert room == 0 && roomPosition >= 0 && roomPosition <= 10
                    || room > 0 && roomPosition >= 1 && roomPosition <= 99;
        }

        int distance(Position other) {
            if (this.room == other.room) {
                return Math.abs(this.roomPosition - other.roomPosition);
            }
            var sum = 0;
            var hallPosition1 = this.roomPosition;
            var hallPosition2 = other.roomPosition;
            if (this.room > 0) {
                sum += this.roomPosition;
                hallPosition1 = this.room * 2;

            }
            if (other.room > 0) {
                sum += other.roomPosition;
                hallPosition2 = other.room * 2;
            }

            return sum + Math.abs(hallPosition1 - hallPosition2);
        }
    }

    record GameStateComponent(Position position, char amphipod) {
        GameStateComponent {
            assert amphipod >= 'A' && amphipod <= 'D';
        }
    }

    record Move(GameStateComponent from, GameStateComponent to) {
        @Override
        public String toString() {
            return "Move " + from.amphipod +
                    ": (" + from.position.room + ", " + from.position.roomPosition + ")"
                    + " (" + to.position.room + ", " + to.position.roomPosition + ")";
        }
    }

    record GameState(Set<GameStateComponent> components, int roomSize) {
        static GameState parseGameState(List<String> text) {
            var components = new HashSet<GameStateComponent>();
            var hallway = text.get(1);
            for (var i = 0; i < 11; i++) {
                char ch = hallway.charAt(i + 1);
                if (ch != '.') {
                    components.add(new GameStateComponent(new Position(0, i), ch));
                }
            }
            for (var i = 0; i < 4; i++) {
                for (var j = 2; j < text.size() - 1; j++) {
                    char ch = text.get(j).charAt(3 + i * 2);
                    if (ch != '.') {
                        components.add(new GameStateComponent(new Position(i + 1, j - 1), ch));
                    }
                }
            }
            return new GameState(components, text.size() - 3);
        }

        List<Character> roomOccupants(int room) {
            return components.stream().filter(component -> component.position.room == room)
                    .sorted(Comparator.comparingInt(c -> c.position.roomPosition))
                    .map(component -> component.amphipod).collect(Collectors.toList());
        }

        boolean canMoveToRoom(int room) {
            var occupants = roomOccupants(room);
            return occupants.stream().map(ch -> ch.equals((char) ('A' + room - 1)))
                    .reduce(true, Boolean::logicalAnd);
        }

        boolean roomToRoomPathIsClear(int room1, int room2) {
            return hallwayPathIsClear(room1 * 2, room2 * 2);
        }

        boolean hallwayPathIsClear(int hallPosition1, int hallPosition2) {
            var p1 = Math.min(hallPosition1, hallPosition2);
            var p2 = Math.max(hallPosition1, hallPosition2);
            var positions = getOccupiedHallwayPositions();
            for (var i = p1; i < p2; i++) {
                if (positions.contains(new Position(0, i)))
                    return false;
            }
            return true;
        }

        Optional<Character> getOccupant(Position p) {
            return components.stream().filter(component -> component.position.equals(p)).map(component -> component.amphipod).findFirst();
        }

        Set<Position> getOccupiedHallwayPositions() {
            return components.stream().filter(component -> component.position.room == 0).map(component -> component.position).collect(Collectors.toSet());
        }

        Set<Position> getMovableHallPositions(int start) {
            var result = new HashSet<Position>();
            var occupiedPositions = getOccupiedHallwayPositions();
            for (var i = start + 1; i <= 10; i++) {
                var pos = new Position(0, i);
                if (occupiedPositions.contains(pos))
                    break;
                if (!(i == 2 || i == 4 || i == 6 || i == 8))
                    result.add(pos);
            }
            for (var i = start - 1; i >= 0; i--) {
                var pos = new Position(0, i);
                if (occupiedPositions.contains(pos))
                    break;
                if (!(i == 2 || i == 4 || i == 6 || i == 8))
                    result.add(pos);
            }
            return result;
        }


        Set<Move> moves() {
            var result = new HashSet<Move>();
            var movableComponents = new HashSet<>(components);

            // evaluate what components are movable
            for (var component : components) {
                if (component.position.room > 0) {
                    var occupants = roomOccupants(component.position.room);
                    var areTopMost = component.position.roomPosition <= roomSize - occupants.size() + 1;
                    if (!areTopMost) {
                        movableComponents.remove(component);
                    } else {
                        var homeRoom = (component.amphipod - 'A' + 1);
                        if (homeRoom == component.position.room) {
                            if (occupants.stream().filter(ch -> !ch.equals(component.amphipod)).findFirst().isEmpty()) {
                                movableComponents.remove(component);
                            }
                        }
                    }
                    if (component.position.roomPosition == 0 && roomOccupants(component.position.room).size() == 2) {
                        // we're blocked, no moves available
                        movableComponents.remove(component);
                    }
                }
            }

            // to shorten paths under evaluation, with prejudice, we discard other moves where
            // a final move completion move is possible from any location.
            for (var component : movableComponents) {
                if (component.position.room > 0) {
                    var homeRoom = component.amphipod - 'A' + 1;
                    if (canMoveToRoom(homeRoom) && roomToRoomPathIsClear(homeRoom, component.position.room)) {
                        var newPosition = new Position(homeRoom, roomSize - roomOccupants(homeRoom).size());
                        return Set.of(new Move(component, new GameStateComponent(newPosition, component.amphipod)));
                    }
                }
                if (component.position.room == 0) {
                    var homeRoom = component.amphipod - 'A' + 1;
                    var destinationHallRoom = homeRoom * 2;
                    // we bump the startingRoom position by one, so we don't test component.position itself
                    // which is of course occupied.
                    var startingRoom = component.position.roomPosition + (
                            destinationHallRoom > component.position.roomPosition ? 1 : -1
                    );
                    if (canMoveToRoom(homeRoom) && hallwayPathIsClear(startingRoom, destinationHallRoom)) {
                        var newPosition = new Position(homeRoom, roomSize - roomOccupants(homeRoom).size());
                        return Set.of(new Move(component, new GameStateComponent(newPosition, component.amphipod)));
                    }
                }
            }


            // out-from-hallway moves
            for (var component : movableComponents) {
                if (component.position.room == 0) {
                    var homeRoom = component.amphipod - 'A' + 1;
                    var occupants = roomOccupants(homeRoom);
                    if (!canMoveToRoom(homeRoom))
                        // if room does not meet criteria to move into it, no move possible
                        continue;
                    // check pathway is clear
                    var hallPositionDestination = homeRoom * 2;
                    // extra +1/-1 here is to avoid testing the present location (which is occupied by component)
                    var hallPositionStart = component.position.roomPosition + (
                            hallPositionDestination < component.position.roomPosition ? -1 : 1);
                    var hpMin = Math.min(hallPositionStart, hallPositionDestination);
                    var hpMax = Math.max(hallPositionStart, hallPositionDestination);
                    var theWayIsClear = true;
                    for (var i = hpMin; theWayIsClear && i <= hpMax; i++) {
                        if (getOccupant(new Position(component.position.room, i)).isPresent())
                            theWayIsClear = false;
                    }
                    if (theWayIsClear) {
                        var newPosition = new Position(homeRoom, roomSize - occupants.size());
                        result.add(new Move(component, new GameStateComponent(newPosition, component.amphipod)));
                    }
                }
            }

            // room to hallways moves
            for (var component : movableComponents) {
                if (component.position.room > 0) {
                    var roomHallPosition = component.position.room * 2;
                    getMovableHallPositions(roomHallPosition).stream().map(
                                    pos -> new Move(component, new GameStateComponent(pos, component.amphipod)))
                            .collect(Collectors.toCollection(() -> result));
                }
            }
            return result;
        }

        boolean done() {
            var correctPositionCount = 0;
            for (var component : components) {
                if (component.position.room == 0)
                    return false;
                if (component.amphipod - 'A' + 1 == component.position.room)
                    correctPositionCount++;
            }
            return correctPositionCount == roomSize * 4;
        }

        @Override
        public String toString() {
            var map = new String[]{
                    "#############",
                    "#...........#",
                    "###.#.#.#.###",
                    "  #.#.#.#.#",
                    "  #########"};

            var hallways = new StringBuilder(map[1]);
            var rooms1 = new StringBuilder(map[2]);
            var rooms2 = new StringBuilder(map[3]);
            for (var component : components) {
                if (component.position.room == 0) {
                    hallways.setCharAt(component.position.roomPosition + 1, component.amphipod);
                } else if (component.position.roomPosition == 0) {
                    rooms2.setCharAt(component.position.room * 2 + 1, component.amphipod);
                } else if (component.position.roomPosition == 1) {
                    rooms1.setCharAt(component.position.room * 2 + 1, component.amphipod);
                }
            }

            return "GameState:\n" +
                    map[0] + '\n' +
                    hallways + '\n' +
                    rooms1 + '\n' +
                    rooms2 + '\n' +
                    map[4] + '\n';
        }
    }

    record PrioritisedGameState(GameState gs, int cost) {
        static PrioritisedGameState create(GameState priorState, int priorCost, Move move) {
            var s = new HashSet<>(priorState.components);
            s.remove(move.from);
            s.add(move.to);
            var newGameState = new GameState(s, priorState.roomSize);
            var multiplier = 1;
            for (var i = 'A'; i < move.from.amphipod; i++)
                multiplier *= 10;
            var moveCost = move.from.position.distance(move.to.position) * multiplier;
            return new PrioritisedGameState(newGameState, priorCost + moveCost);
        }
    }

}
