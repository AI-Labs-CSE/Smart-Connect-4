# Connect-4 AI Agent
A full game with GUI with only one mode
human vs. computer where we choose whether to use **alpha-beta pruning**
or not in AI agent then play against the agent till the board is full. The
game dimensions are as follows width = 7 and height = 6.

## Features 
- Game GUI
- Supporting two algorithms for the AI agent:
  - Minimax without alpha-beta pruning
  - Minimax with alpha-beta pruning
- Showing the minimax tree in each turn
- Design a suitable heuristic function

## Heuristic assumption
The strategy of calculating the heuristic in this problem was to put power to
each cell in the game and return a value that represents if the red is in good
condition +ve number of if the red is in bad possession -ve number.

If red are behind each other then they have more power also if red pieces have
empty around its power will be more than the power of the red surrounded by
yellow Pisces.

We will iterate column by column for all possible nonempty cells and if the cell
is red for example we will check the cells near that cell if one of the near cells is
red we will add to the number 15 and if it is empty we will add 10 but not
possible to play in it for the next step we will give it 5.

![image](https://github.com/AI-Labs-CSE/Smart-Connect-4/assets/73740339/933aeeaf-cb40-4260-bf10-ec8c9662e695)

In addition, we will add the scour difference multiplied by 642 so that we will
give the scour more priority than the other heuristic.

## State Representation
We represent the state in only one long number each column has 9 bits the first least
significant bits are for the column's current length and the other 6 bits represent the column.

EX: The state is something like
``101011101011010111001011101111010111110011101100010111001011101``

Let’s extract the first column which is the first 9 least significant bits ``001011 101``
that means that this column has a length of 5 and the pieces in that column are
{ ``red``, ``red``, ``yellow``, ``red``, ``yellow`` } from bottom to peak.

So zeros represent yellow and ones represent red.

## Used Algorithms
### Minimax without pruning Pseudocode
```python
value(state, nextAgent, curDepth):
    increment the expanded nodes
    if current depth > max depth:
        return heuristic + score difference
    if current state == terminal:
        return score difference
    if next agent == maximizer:
        return maxValue(state, curDepth)
    else:
        return minValue(state, curDepth)
```
```python
maxValue(state, curDepth):
    initialize the score with -∞
    for successor in current state:
        utility = value(each successor, minimizer, curDepth+1)
        score = max(utility, score)
    optimalMap.put(score)
    return score
```
```python
minValue():
    initialize the score with +∞
    for successor in current state:
        utility = value(each successor, maximizer, curDepth+1)
        score = min(utility, score)
    optimalMap.put(score)
    return score
```
## Sample Runs
- without pruning with depth 2 ![image](https://github.com/AI-Labs-CSE/Smart-Connect-4/assets/73740339/7a85ace8-9813-4168-b12a-b49699f53be8)
- With Alpha Beta pruning with depth 2 ![image](https://github.com/AI-Labs-CSE/Smart-Connect-4/assets/73740339/cac17d46-e2a6-4c06-9147-ce4a2ff9a9aa)

## Analysis
### Without Pruning
Depth | Expanded Nodes | Time(seconds) |
--- | --- | --- |
6 | 137180 | 0.29 |
7 | 959078 | 1.367 |
8 | 6710260 | 10.179 |
9 | 47050206 | 74.814 |

### With Pruning
Depth | Expanded Nodes | Time(seconds) |
--- | --- | --- |
6 | 13362 | 0.02 |
7 | 116302 | 0.18 |
8 | 158444 | 0.24 |
9 | 595694 | 0.9 |

### Graphs
![image](https://github.com/AI-Labs-CSE/Smart-Connect-4/assets/73740339/8e34aaa7-8ef1-4065-ba5c-de0c9b2d796e)
