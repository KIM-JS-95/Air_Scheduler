
import 'package:flutter/material.dart';

import '../../utils/r.dart';
import 'fading_item_list_controller.dart';

class FadingItemList extends StatefulWidget {
  final List<Widget> listItems;
  final FadingItemListController fadingItemListController;

  const FadingItemList({
    Key? key,
    required this.listItems,
    required this.fadingItemListController,
  }) : super(key: key);

  @override
  State<FadingItemList> createState() => _FadingItemListState();
}

class _FadingItemListState extends State<FadingItemList>
    with TickerProviderStateMixin {
  late final Map<int, AnimatedListItemData> _listItemsMap = {};

  @override
  void initState() {
    super.initState();
    _initValues();
  }

  void _initValues() {
    final controllers = <AnimationController>[];

    for (int i = 0; i < widget.listItems.length; i++) {
      final itemAnimContr = AnimationController(
        vsync: this,
        duration: const Duration(milliseconds: 200),
      );
      _listItemsMap[i] = AnimatedListItemData(
        _buildAnimatedListItem(widget.listItems[i], itemAnimContr),
        itemAnimContr,
      );

      controllers.add(itemAnimContr);
    }

    widget.fadingItemListController.controllers = controllers.toList();
  }


  @override
  Widget build(BuildContext context) => Stack(
    alignment: Alignment.bottomCenter,
    children: [
      ListView.builder(
        itemCount: _listItemsMap.length,
        itemBuilder: (ctx, i) => _listItemsMap[i]!.widget,
      ),
    ],
  );

  Widget _buildAnimatedListItem(
      Widget listItem,
      AnimationController controller,
      ) =>
      AnimatedBuilder(
        animation: controller,
        builder: (ctx, child) => Opacity(
          opacity: controller.value * 1.0,
          child: listItem,
        ),
      );

  @override
  void dispose() {
    _listItemsMap.values.map((e) => e.animationController.dispose());
    super.dispose();
  }
}

class AnimatedListItemData {
  final Widget widget;
  final AnimationController animationController;

  AnimatedListItemData(this.widget, this.animationController);
}