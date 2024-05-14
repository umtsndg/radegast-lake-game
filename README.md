Radegast's game is a game where there is a world which is created by blocks.

The size of the world is determined by the first input. For example assume the first line is "m n": It means the world is a rectangle with edges m and n.
Next part of the input is an mxn matrix which describes the height levels of each chunk in the world.
Final part is the modifications. The coordinates' height levels that are given in this parts are lowered by one level.

After the input taking part is finished, the alghoritm simulates as if an infinite water source is drained from the top onto the world and finds and labels all the lakes that are formed by this operation.
Finally the code outputs the final status of the world with the lakes labeled and displays the score formed by lakes.
