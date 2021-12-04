/*mpsz=['m','p','s','z'];
basic_hand=[[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]];
initialize_santen(convert_to_card("123m46p5789s11122z"),basic_mountain)
function put_card(card_numbers,hand_color,worded)
{
	if(card_numbers.length==0)
		return false;
	while(card_numbers.length>0)
	{
		let the_number=card_numbers.pop();
		if(worded&&the_number>=7)
			return false;
		if(the_number==9)
			the_number=4;
		if(++hand_color[the_number]>4)
			return false;
	}
	return true;
}

function put_number(card_numbers,the_number)
{
	switch(the_number)
	{
		case '0':card_numbers.push(9);return true;
		case '1':card_numbers.push(0);return true;
		case '2':card_numbers.push(1);return true;
		case '3':card_numbers.push(2);return true;
		case '4':card_numbers.push(3);return true;
		case '5':card_numbers.push(4);return true;
		case '6':card_numbers.push(5);return true;
		case '7':card_numbers.push(6);return true;
		case '8':card_numbers.push(7);return true;
		case '9':card_numbers.push(8);return true;
		default:return false;
	}
}

function convert_to_card(hand_string)
{
	let card_numbers=[];
	let hand=JSON.parse(JSON.stringify(basic_hand));
	for(let i=0;i<hand_string.length;++i)
	{
		switch(hand_string[i])
		{
			case 'm':if(!put_card(card_numbers,hand[0],false))return basic_hand;break;
			case 'p':if(!put_card(card_numbers,hand[1],false))return basic_hand;break;
			case 's':if(!put_card(card_numbers,hand[2],false))return basic_hand;break;
			case 'z':if(!put_card(card_numbers,hand[3],true))return basic_hand;break;
			default:if(!put_number(card_numbers,hand_string[i]))return basic_hand;
		}
	}
	return hand;
}

function convert_to_hold(card)
{
	return mpsz.indexOf(card[1])*9+((card[0]=="0"&&card[1]!="z")?4:(card[0]-"1"));
}*/


//优化向听
function initialize_santen(hand, residue) {
	let U = [[0, 0, 0, 0, 0], [0, 0, 0, 0, 0], [0, 0, 0, 0, 0], [0, 0, 0, 0, 0]];
	let T = [[0, 0, 0, 0, 0], [0, 0, 0, 0, 0], [0, 0, 0, 0, 0], [0, 0, 0, 0, 0]];
	for (let i = 0; i < 4; ++i) {
		let the_U_result = best_decompose_without_quetou(hand[i], i, residue[i]);
		//the_U_result第0项是面子数，第1项是有效搭子数，第2项是孤张数
		for (let j = 0; j < 5; ++j) {
			U[i][j] = ((the_U_result[0][1] >= j) ? 0 : ((the_U_result[1][1] >= j - the_U_result[0][1]) ? (j - the_U_result[0][1]) : ((the_U_result[2][1] >= j - the_U_result[0][1] - the_U_result[1][1]) ? (the_U_result[1][1] + 2 * (j - the_U_result[0][1] - the_U_result[1][1])) : (the_U_result[1][1] + 2 * the_U_result[2][1] + 3 * (j - the_U_result[0][1] - the_U_result[1][1] - the_U_result[2][1])))));
			if ((the_U_result[0][1] > j) || (the_U_result[1][1] > j - the_U_result[0][1]))
				T[i][j] = U[i][j] + 1;
			else if (the_U_result[2][1] > j - the_U_result[0][1] - the_U_result[1][1])//检查孤张是否是虚听雀头
			{
				for (let k = 0; k <= ((i == 3) ? 7 : 9); ++k) {
					if (k == ((i == 3) ? 7 : 9)) {
						T[i][j] = U[i][j] + 2;
						break;
					}
					if (the_U_result[2][0][k] > 0 && residue[i][k] > 0) {
						T[i][j] = U[i][j] + 1;
						break;
					}
				}
			}
			else
				T[i][j] = U[i][j] + 2;
		}
		for (let k = 0; k < ((i == 3) ? 7 : 9); ++k) {
			if (hand[i][k] >= 2) {
				let hand_copy = JSON.parse(JSON.stringify(hand[i]));
				hand_copy[k] -= 2;
				let the_T_result = best_decompose_without_quetou(hand_copy, i, residue[i]);
				for (let j = 0; j < 5; ++j) {
					let temp_result = ((the_T_result[0][1] >= j) ? 0 : ((the_T_result[1][1] >= j - the_T_result[0][1]) ? (j - the_T_result[0][1]) : ((the_T_result[2][1] >= j - the_T_result[0][1] - the_T_result[1][1]) ? (the_T_result[1][1] + 2 * (j - the_T_result[0][1] - the_T_result[1][1])) : (the_T_result[1][1] + 2 * the_T_result[2][1] + 3 * (j - the_T_result[0][1] - the_T_result[1][1] - the_T_result[2][1])))));
					if (temp_result < T[i][j])
						T[i][j] = temp_result;
				}
			}
		}
	}
	return [U, T];
}

function anti(color_hand, color) {
	let anti_hand = [];
	for (let j = 0; j < ((color == 3) ? 7 : 9); ++j)
		anti_hand[j] = 4 - color_hand[j];
	return anti_hand;
}

function best_decompose_without_quetou(color_hand, color, color_residue = anti(color_hand, color)) {
	//console.log(JSON.stringify(color_residue));
	let mianzi_amount = 0, the_mianzi_decompose = 0, mianzi_list = mianzi_decompose_without_quetou(color_hand, color);
	for (let k = 0; k < mianzi_list.length; ++k)
		if (mianzi_list[k].length > mianzi_amount) {
			mianzi_amount = mianzi_list[k].length;
			the_mianzi_decompose = k;
		}
	let hand_copy = JSON.parse(JSON.stringify(color_hand));
	for (let k = 0; k < mianzi_list[the_mianzi_decompose].length; ++k) {
		let mianzi_type = mianzi_list[the_mianzi_decompose][k];
		switch (mianzi_type[1]) {
			case 1: hand_copy[mianzi_type[0]] -= 3; break;
			case 2: hand_copy[mianzi_type[0]] -= 1; hand_copy[mianzi_type[0] + 1] -= 1; hand_copy[mianzi_type[0] + 2] -= 1; break;
			default: ;
		}
	}
	let dazi_amount = 0, the_dazi_decompose = 0, dazi_list = dazi_decompose_without_quetou(hand_copy, color, color_residue);
	for (let k = 0; k < dazi_list.length; ++k)
		if (dazi_list[k].length > dazi_amount) {
			dazi_amount = dazi_list[k].length;
			the_dazi_decompose = k;
		}
	let hand_copy_copy = JSON.parse(JSON.stringify(hand_copy));
	for (let k = 0; k < dazi_list[the_dazi_decompose].length; ++k) {
		let dazi_type = dazi_list[the_dazi_decompose][k];
		switch (dazi_type[1]) {
			case 1: hand_copy_copy[dazi_type[0]] -= 2; break;
			case 2: hand_copy_copy[dazi_type[0]] -= 1; hand_copy_copy[dazi_type[0] + 2] -= 1; break;
			case 3: hand_copy_copy[dazi_type[0]] -= 1; hand_copy_copy[dazi_type[0] + 1] -= 1; break;
			default: ;
		}
	}
	let final_amount = 0;
	for (let k = 0; k < hand_copy_copy.length; ++k)
		final_amount += hand_copy_copy[k];
	return [[mianzi_list[the_mianzi_decompose], mianzi_list[the_mianzi_decompose].length], [dazi_list[the_dazi_decompose], dazi_list[the_dazi_decompose].length], [hand_copy_copy, final_amount]];
}

function mianzi_decompose_without_quetou(color_hand, color, last_j = 0, last_step = 0) {
	let mianzi_list = [];
	let card_amount = 0;
	let j;
	for (j = 0; j < ((color == 3) ? 7 : 9); ++j)
		card_amount += color_hand[j];
	if (card_amount == 0)
		return [[]];
	for (j = 0; j < ((color == 3) ? 7 : 9); ++j)
		if (color_hand[j] >= 1)
			break;
	if (color_hand[j] >= 3 && (j != last_j || last_step <= 1)) {
		let hand_copy = JSON.parse(JSON.stringify(color_hand));
		hand_copy[j] -= 3;
		let other_mianzi = mianzi_decompose_without_quetou(hand_copy, color, j, 1);
		for (let k = 0; k < other_mianzi.length; ++k) {
			other_mianzi[k].unshift([j, 1, "" + (j + 1) + (j + 1) + (j + 1) + mpsz[color]]);
			mianzi_list.unshift(other_mianzi[k]);
		}
	}
	if (color < 3 && color_hand[j + 1] >= 1 && color_hand[j + 2] >= 1 && (j != last_j || last_step <= 2)) {
		let hand_copy = JSON.parse(JSON.stringify(color_hand));
		hand_copy[j] -= 1;
		hand_copy[j + 1] -= 1;
		hand_copy[j + 2] -= 1;
		let other_mianzi = mianzi_decompose_without_quetou(hand_copy, color, j, 2);
		for (let k = 0; k < other_mianzi.length; ++k) {
			other_mianzi[k].unshift([j, 2, "" + (j + 1) + (j + 2) + (j + 3) + mpsz[color]]);
			mianzi_list.unshift(other_mianzi[k]);
		}
	}
	if (JSON.stringify(mianzi_list) == "[]" || JSON.stringify(mianzi_list) == "[[]]") {
		//console.log("Remove "+(j+1)+mpsz[color]);
		let hand_copy = JSON.parse(JSON.stringify(color_hand));
		hand_copy[j] -= 1;
		let other_mianzi = mianzi_decompose_without_quetou(hand_copy, color, j, 3);
		for (let k = 0; k < other_mianzi.length; ++k)
			mianzi_list.unshift(other_mianzi[k]);
	}
	return mianzi_list;
}

function dazi_decompose_without_quetou(color_hand, color, color_residue, last_j = 0, last_step = 0) {
	//console.log(JSON.stringify(color_hand));
	let dazi_list = [];
	let card_amount = 0;
	let j;
	for (j = 0; j < ((color == 3) ? 7 : 9); ++j)
		card_amount += color_hand[j];
	if (card_amount == 0)
		return [[]];
	for (j = 0; j < ((color == 3) ? 7 : 9); ++j)
		if (color_hand[j] >= 1)
			break;
	if (color_hand[j] >= 2 && color_residue[j] >= 1 && (j != last_j || last_step <= 1)) {
		let hand_copy = JSON.parse(JSON.stringify(color_hand));
		let residue_copy = JSON.parse(JSON.stringify(color_residue));
		hand_copy[j] -= 2;
		residue_copy[j] -= 1;
		let other_dazi = dazi_decompose_without_quetou(hand_copy, color, residue_copy, j, 1);
		for (let k = 0; k < other_dazi.length; ++k) {
			other_dazi[k].unshift([j, 1, "" + (j + 1) + (j + 1) + mpsz[color]]);
			dazi_list.unshift(other_dazi[k]);
		}
	}
	//console.log(""+j+" "+color_hand[j+2]+" "+color_residue[j+1]);
	if (color < 3 && j < 7 && color_hand[j + 2] >= 1 && color_residue[j + 1] >= 1 && (j != last_j || last_step <= 2)) {
		//console.log(""+(j+1)+(j+3)+mpsz[color]);
		let hand_copy = JSON.parse(JSON.stringify(color_hand));
		let residue_copy = JSON.parse(JSON.stringify(color_residue));
		hand_copy[j] -= 1;
		hand_copy[j + 2] -= 1;
		residue_copy[j + 1] -= 1;
		let other_dazi = dazi_decompose_without_quetou(hand_copy, color, residue_copy, j, 2);
		for (let k = 0; k < other_dazi.length; ++k) {
			other_dazi[k].unshift([j, 2, "" + (j + 1) + (j + 3) + mpsz[color]]);
			dazi_list.unshift(other_dazi[k]);
		}
	}
	if (color < 3 && j < 8 && color_hand[j + 1] >= 1 && ((j > 0 && color_residue[j - 1] >= 1) || (j < 7 && color_residue[j + 2] >= 1)) && (j != last_j || last_step <= 3)) {
		let hand_copy = JSON.parse(JSON.stringify(color_hand));
		let residue_copy = JSON.parse(JSON.stringify(color_residue));
		hand_copy[j] -= 1;
		hand_copy[j + 1] -= 1;
		if (j > 0 && residue_copy[j - 1] >= 1)
			residue_copy[j - 1] -= 1;
		else
			residue_copy[j + 2] -= 1
		let other_dazi = dazi_decompose_without_quetou(hand_copy, color, residue_copy, j, 3);
		for (let k = 0; k < other_dazi.length; ++k) {
			other_dazi[k].unshift([j, 3, "" + (j + 1) + (j + 2) + mpsz[color]]);
			dazi_list.unshift(other_dazi[k]);
		}
	}
	if (JSON.stringify(dazi_list) == "[]" || JSON.stringify(dazi_list) == "[[]]") {
		//console.log("Remove "+(j+1)+mpsz[color]);
		let hand_copy = JSON.parse(JSON.stringify(color_hand));
		hand_copy[j] -= 1;
		let other_dazi = dazi_decompose_without_quetou(hand_copy, color, color_residue, j, 4);
		for (let k = 0; k < other_dazi.length; ++k)
			dazi_list.unshift(other_dazi[k]);
	}
	return dazi_list;
}

//console.log(JSON.stringify(best_decompose_without_quetou(convert_to_card("23799m")[0],0,anti(convert_to_card("1111222234444799m")[0],0))))