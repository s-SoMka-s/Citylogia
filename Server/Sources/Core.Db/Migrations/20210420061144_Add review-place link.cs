using Microsoft.EntityFrameworkCore.Migrations;

namespace Core.Db.Migrations
{
    public partial class Addreviewplacelink : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_Reviews_PlaceId",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.AlterColumn<long>(
                name: "PlaceId",
                schema: "citylogia",
                table: "Reviews",
                type: "bigint",
                nullable: false,
                defaultValue: 0L,
                oldClrType: typeof(long),
                oldType: "bigint",
                oldNullable: true);

            migrationBuilder.AddColumn<long>(
                name: "PlaceId1",
                schema: "citylogia",
                table: "Reviews",
                type: "bigint",
                nullable: false,
                defaultValue: 0L);

            migrationBuilder.AddColumn<long>(
                name: "PlaceId2",
                schema: "citylogia",
                table: "Reviews",
                type: "bigint",
                nullable: false,
                defaultValue: 0L);

            migrationBuilder.CreateIndex(
                name: "IX_Reviews_PlaceId",
                schema: "citylogia",
                table: "Reviews",
                column: "PlaceId",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_Reviews_PlaceId1",
                schema: "citylogia",
                table: "Reviews",
                column: "PlaceId1");

            migrationBuilder.AddForeignKey(
                name: "FK_Reviews_Places_PlaceId1",
                schema: "citylogia",
                table: "Reviews",
                column: "PlaceId1",
                principalSchema: "citylogia",
                principalTable: "Places",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Reviews_Places_PlaceId1",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.DropIndex(
                name: "IX_Reviews_PlaceId",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.DropIndex(
                name: "IX_Reviews_PlaceId1",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.DropColumn(
                name: "PlaceId1",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.DropColumn(
                name: "PlaceId2",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.AlterColumn<long>(
                name: "PlaceId",
                schema: "citylogia",
                table: "Reviews",
                type: "bigint",
                nullable: true,
                oldClrType: typeof(long),
                oldType: "bigint");

            migrationBuilder.CreateIndex(
                name: "IX_Reviews_PlaceId",
                schema: "citylogia",
                table: "Reviews",
                column: "PlaceId");
        }
    }
}
