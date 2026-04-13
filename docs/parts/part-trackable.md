---
source: https://raw.githubusercontent.com/inventree/InvenTree/master/docs/docs/part/trackable.md
component: parts
topic: Trackable Parts and Serial Numbers
fetched: 2026-04-13
---

# Trackable Parts

## Table of Contents

- [Stock Tracking](#stock-tracking)
- [Assign Serial Numbers](#assign-serial-numbers)
- [Build Orders](#build-orders)

---

## Stock Tracking

Denoting a part as *Trackable* changes the way that stock items associated with the particular part are handled in the database. A trackable part also has more restrictions imposed by the database scheme.

For many parts in an InvenTree database, simply tracking current stock levels (and locations) is sufficient. However, some parts require more extensive tracking than simple stock level knowledge.

Any stock item associated with a trackable part *must* have either a batch number or a serial number. This includes stock created manually or via an internal process (such as a Purchase Order or a Build Order).

## Assign Serial Numbers

Serial numbers (for parts which are marked as trackable) are used in multiple forms and processes in InvenTree.

For faster input there are several ways to define the wanted serial numbers (SN):

| Marker | Input | Result | Description |
| --- | --- | --- | --- |
|  | `1` | `[1]` | single SN |
| , | `1,3,5` | `[1, 3, 5]` | list of SNs |
| - | `1-5` | `[1, 2, 3, 4, 5]` | stretch of SN |
| ~ | `~` (next SN is 8) | `[8]` | represents the next SN |
| `<start>`+ | `4+` (with 3 numbers needed) | `[4, 5, 6]` | all needed SNs from `<start>` |
| `<start>`+`<length>` | `2+2` | `[2, 3, 4]` | `<length>` SNs added to `<start>` |

These rules can be mix-and-matched with whitespaces or commas separating them.

For example:
- `1 3-5 9+2` or `1,3-5,9+2` results in `[1, 3, 4, 5, 9, 10, 11]`
- `~+2` (with next SN being 14) results in `[14, 15, 16]`
- `~+` (with next SN being 14 and 2 numbers needed) results in `[14, 15]`

## Build Orders

Build orders have some extra requirements when either building a trackable part, or using parts in the Bill of Materials which are themselves trackable.
